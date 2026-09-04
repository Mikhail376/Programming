package server;

import common.Request;
import common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.AuthManager;
import server.manager.CollectionManager;
import server.manager.CommandManager;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private final int port;
    private final CommandManager commandManager;
    private final AuthManager authManager;
    private final CollectionManager collectionManager;

    private final ForkJoinPool readPool;
    private final ForkJoinPool processPool;
    private final ExecutorService sendPool;

    private ServerSocket serverSocket;
    private volatile boolean isRunning = true;

    public Server(int port, CommandManager commandManager, AuthManager authManager, CollectionManager collectionManager) {
        this.port = port;
        this.commandManager = commandManager;
        this.authManager = authManager;
        this.collectionManager = collectionManager;

        this.readPool = new ForkJoinPool();
        this.processPool = new ForkJoinPool();
        this.sendPool = Executors.newFixedThreadPool(10);
    }

    public void run() {
        new Thread(this::consoleInputListener, "ServerConsole").start();

        try {
            serverSocket = new ServerSocket(port);
            logger.info("Сервер запущен на порту {}.", port);

            while (isRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    logger.info("Новое подключение: {}", clientSocket.getRemoteSocketAddress());
                    ClientConnection connection = new ClientConnection(clientSocket);
                    readPool.execute(new ReadTask(connection));
                } catch (SocketException e) {
                    if (!isRunning) break;
                    logger.error("Ошибка принятия соединения: {}", e.getMessage());
                } catch (IOException e) {
                    logger.error("Ошибка инициализации соединения: {}", e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.error("Критическая ошибка сервера: {}", e.getMessage());
        } finally {
            shutdown();
        }
    }

    private void consoleInputListener() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (isRunning && scanner.hasNextLine()) {
                String line = scanner.nextLine().trim().toLowerCase();
                if (line.equals("exit")) {
                    logger.info("Получена команда exit из консоли. Завершаем работу...");
                    shutdown();
                    break;
                }
            }
        }
    }

    private class ClientConnection {
        private final Socket socket;
        private final ObjectOutputStream oos;
        private final ObjectInputStream ois;
        private final Object writeLock = new Object();

        ClientConnection(Socket socket) throws IOException {
            this.socket = socket;
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.oos.flush();
            this.ois = new ObjectInputStream(socket.getInputStream());
        }

        Request readRequest() throws IOException, ClassNotFoundException {
            return (Request) ois.readObject();
        }

        void send(Response response) {
            synchronized (writeLock) {
                try {
                    oos.writeObject(response);
                    oos.reset();
                    oos.flush();
                } catch (IOException e) {
                    logger.error("Ошибка отправки ответа {}: {}", socket.getRemoteSocketAddress(), e.getMessage());
                    closeSocket(socket);
                }
            }
        }

        Socket getSocket() {
            return socket;
        }
    }

    private class ReadTask implements Runnable {
        private final ClientConnection connection;

        ReadTask(ClientConnection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            Socket socket = connection.getSocket();
            try {
                while (isRunning && !socket.isClosed()) {
                    try {
                        Request request = connection.readRequest();
                        logger.debug("Запрос получен от {}: {}", socket.getRemoteSocketAddress(), request.getCommandName());
                        processPool.execute(new ProcessTask(connection, request));
                    } catch (EOFException | SocketException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.error("Ошибка чтения от {}: {}", socket.getRemoteSocketAddress(), e.getMessage());
            } finally {
                closeSocket(socket);
            }
        }
    }

    private class ProcessTask implements Runnable {
        private final ClientConnection connection;
        private final Request request;

        ProcessTask(ClientConnection connection, Request request) {
            this.connection = connection;
            this.request = request;
        }

        @Override
        public void run() {
            String commandName = request.getCommandName();

            if (!commandName.equals("register") && !commandName.equals("help")) {
                if (request.getUsername() == null || request.getPasswordHash() == null) {
                    sendPool.execute(() -> connection.send(new Response("Ошибка: не указаны данные для авторизации.")));
                    return;
                }
                if (!authManager.authenticate(request.getUsername(), request.getPasswordHash())) {
                    sendPool.execute(() -> connection.send(new Response("Ошибка авторизации: Неверный логин или пароль.")));
                    return;
                }
            }

            try {
                Response response = commandManager.execute(request);
                sendPool.execute(() -> connection.send(response));
            } catch (Exception e) {
                sendPool.execute(() -> connection.send(new Response("Ошибка выполнения: " + e.getMessage())));
            }
        }
    }

    private void closeSocket(Socket socket) {
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {}
    }

    public void shutdown() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
        } catch (IOException ignored) {}
        readPool.shutdownNow();
        processPool.shutdownNow();
        sendPool.shutdownNow();
        logger.info("Сервер остановлен.");
    }
}