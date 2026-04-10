package server;

import common.Request;
import common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.commands.SaveCommand;
import server.manager.CommandManager;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final int port;
    private final CommandManager commandManager;
    private Selector selector;
    private volatile boolean isRunning = true;

    public Server(int port, CommandManager commandManager) {
        this.port = port;
        this.commandManager = commandManager;
    }

    public void run() {
        try {
            selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            logger.info("Сервер успешно запущен на порту {}", port);

            startConsoleThread();

            while (isRunning) {
                if (selector.select(100) == 0) continue;

                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (!key.isValid()) continue;

                    if (key.isAcceptable()) {
                        acceptConnection(key);
                    } else if (key.isReadable()) {
                        processClientRequest(key);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Критическая ошибка сервера: {}", e.getMessage());
        } finally {
            stop();
        }
    }

    private void startConsoleThread() {
        Thread t = new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            logger.info("Консоль сервера готова. Доступные команды: save, exit");
            while (isRunning && sc.hasNextLine()) {
                String line = sc.nextLine().trim().toLowerCase();
                if (line.equals("exit")) {
                    executeServerSave();
                    isRunning = false;
                    if (selector != null) selector.wakeup();
                } else if (line.equals("save")) {
                    executeServerSave();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void executeServerSave() {
        var save = commandManager.getCommands().get("save");
        if (save instanceof SaveCommand s) {
            s.serverExecute();
            logger.info("Коллекция принудительно сохранена сервером.");
        }
    }

    private void acceptConnection(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel client = ssc.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        logger.info("Установлено новое соединение: {}", client.getRemoteAddress());
    }

    private void processClientRequest(SelectionKey key) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(32768);

        try {
            int bytesRead = clientChannel.read(buffer);

            if (bytesRead == -1) {
                logger.info("Клиент {} разорвал соединение", clientChannel.getRemoteAddress());
                clientChannel.close();
                key.cancel();
                return;
            }
            if (bytesRead == 0) return;

            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);

            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                Request request = (Request) ois.readObject();
                logger.info("Выполнение команды '{}' от клиента {}",
                        request.getCommandName(), clientChannel.getRemoteAddress());

                Response response = commandManager.execute(request);
                sendResponse(clientChannel, response);

            } catch (StreamCorruptedException e) {
                logger.error("Ошибка десериализации: поврежден заголовок потока от {}", clientChannel.getRemoteAddress());
            } catch (ClassNotFoundException e) {
                logger.error("Получен объект неизвестного класса от {}", clientChannel.getRemoteAddress());
            }

        } catch (IOException e) {
            logger.error("Ошибка при чтении данных от клиента: {}", e.getMessage());
            try { clientChannel.close(); } catch (IOException ignored) {}
            key.cancel();
        }
    }

    private void sendResponse(SocketChannel clientChannel, Response response) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(response);
            oos.flush();
        }

        byte[] responseData = baos.toByteArray();
        ByteBuffer buffer = ByteBuffer.wrap(responseData);

        while (buffer.hasRemaining()) {
            clientChannel.write(buffer);
        }
        logger.info("Ответ успешно отправлен на {}", clientChannel.getRemoteAddress());
    }

    private void stop() {
        try {
            if (selector != null) selector.close();
            logger.info("Работа сервера завершена.");
        } catch (IOException e) {
            logger.error("Ошибка при закрытии селектора: {}", e.getMessage());
        }
    }
}