package client;

import client.commands.ExecuteScriptCommand;
import common.Request;
import common.Response;
import common.model.Dragon;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Client {
    private final String host;
    private final int port;
    private final InputManager inputManager;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String username;
    private String passwordHash;

    private final Set<String> objectCommands = new HashSet<>(Arrays.asList(
            "insert", "update", "replace_if_greater", "remove_greater"
    ));

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        this.inputManager = new InputManager(new Scanner(System.in));
    }

    public void run() {
        System.out.println("=== Клиент запущен ===");

        System.out.print("Введите логин: ");
        username = inputManager.readLine();
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Логин обязателен для работы.");
            System.exit(1);
        }

        System.out.print("Введите пароль: ");
        String pass = inputManager.readLine();
        if (pass == null) pass = "";
        passwordHash = hashSHA224(pass);

        System.out.println("Если вы новый пользователь, введите команду 'register' для создания аккаунта.");
        System.out.println("Если аккаунт уже есть, вы можете сразу вводить команды (например, 'help' или 'show').");
        System.out.println("--------------------------------------------");

        while (true) {
            try {
                if (socket == null || socket.isClosed() || !socket.isConnected()) {
                    connect();
                }
                System.out.print("> ");
                String line = inputManager.readLine();
                if (line == null) break;
                processSingleCommand(line);
            } catch (IOException e) {
                System.err.println("Связь с сервером потеряна. Повторное подключение через 2 сек...");
                close();
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            }
        }
        close();
    }

    public void processSingleCommand(String line) throws IOException {
        String[] parts = line.trim().split("\\s+", 2);
        String cmd = parts[0].toLowerCase();
        String arg = parts.length > 1 ? parts[1] : null;
        if (cmd.isEmpty()) return;

        if (cmd.equals("exit")) {
            System.exit(0);
        }
        if (cmd.equals("execute_script")) {
            new ExecuteScriptCommand(inputManager, this).execute(arg);
            return;
        }
        if (cmd.equals("save")) {
            System.out.println("Команда save доступна только на сервере.");
            return;
        }

        try {
            Dragon dragon = null;
            if (objectCommands.contains(cmd)) {
                dragon = inputManager.readDragon(username);
                if (dragon == null) return;
            }

            Request request = new Request(cmd, arg, dragon, username, passwordHash);
            sendRequest(request);
            Response response = receiveResponse();
            printResponse(response);
        } catch (ClassNotFoundException e) {
            System.err.println("Ошибка десериализации ответа от сервера.");
        }
    }

    private void sendRequest(Request request) throws IOException {
        oos.writeObject(request);
        oos.reset();
        oos.flush();
    }

    private Response receiveResponse() throws IOException, ClassNotFoundException {
        return (Response) ois.readObject();
    }

    private void printResponse(Response response) {
        if (response.getCollection() != null) {
            System.out.println(response.getMessage());
            response.getCollection().forEach(System.out::println);
            System.out.println("============================");
        } else {
            System.out.println(response.getMessage());
        }
    }

    private void connect() throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), 5000);
        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush();
        ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("Успешное соединение с сервером!");
    }

    private String hashSHA224(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-224");
            byte[] hash = md.digest(input.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) hex.append(String.format("%02x", b));
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось хешировать пароль", e);
        }
    }

    private void close() {
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {}
    }

    public static void main(String[] args) {
        new Client("localhost", 8080).run();
    }
}