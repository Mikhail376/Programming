package client;

import client.commands.ExecuteScriptCommand;
import common.Request;
import common.Response;
import common.model.Dragon;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class Client {
    private final String host;
    private final int port;
    private final InputManager inputManager;
    private Socket socket;

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
        while (true) {
            try {
                if (socket == null || socket.isClosed() || !socket.isConnected()) {
                    connect();
                }

                System.out.print("> ");
                String line = inputManager.readLine();
                if (line == null) break; // Ctrl+D

                processSingleCommand(line);

            } catch (IOException e) {
                System.err.println("Ошибка связи с сервером. Повторная попытка через 2 сек");
                close();
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            }
        }
        close();
    }

    public void processSingleCommand(String line) {
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
            Request request = buildRequest(cmd, arg);
            if (request == null) return;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(request);
                oos.flush();
                byte[] data = baos.toByteArray();
                socket.getOutputStream().write(data);
                socket.getOutputStream().flush();
            }


            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Response response = (Response) ois.readObject();

            if (response.getCollection() != null) {
                System.out.println(response.getMessage());
                response.getCollection().forEach(System.out::println);
                System.out.println("============================");
            } else {
                System.out.println(response.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Ошибка при выполнении команды '" + cmd + "': " + e.getMessage());
        }
    }

    private void connect() throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), 5000);
        System.out.println("Успешное соединение с сервером!");
    }

    private Request buildRequest(String command, String argument) {
        try {
            if (objectCommands.contains(command)) {
                Dragon dragon = inputManager.readDragon();
                if (dragon == null) return null;
                return new Request(command, argument, dragon);
            }
            return new Request(command, argument, null);
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка валидации: " + e.getMessage());
            return null;
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