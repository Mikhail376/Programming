package client.commands;

import client.Client;
import client.InputManager;
import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ExecuteScriptCommand {
    private final InputManager inputManager;
    private final Client client;

    private static final Set<String> scriptStack = new HashSet<>();
    private static boolean stopFlag = false;

    public ExecuteScriptCommand(InputManager inputManager, Client client) {
        this.inputManager = inputManager;
        this.client = client;
    }

    public void execute(String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            System.err.println("Ошибка: не указано имя файла.");
            return;
        }

        File file = new File(argument);
        if (!file.exists()) {
            System.err.println("Ошибка: Файл '" + argument + "' не найден.");
            return;
        }

        String path = file.getAbsolutePath();

        if (scriptStack.contains(path)) {
            System.err.println("Критическая ошибка: Рекурсивный вызов скрипта отменен (" + path + ")");
            stopFlag = true;
            return;
        }

        try (Scanner scriptScanner = new Scanner(new FileInputStream(file))) {
            scriptStack.add(path);
            inputManager.pushScanner(scriptScanner);

            System.out.println("Выполнение скрипта: " + file.getName());

            while (scriptScanner.hasNextLine() && !stopFlag) {
                String line = inputManager.readLine();
                if (line == null) break;

                if (line.isEmpty() || line.startsWith("#")) continue;

                client.processSingleCommand(line);
            }

        } catch (IOException e) {
            System.err.println("Ошибка доступа к файлу: " + e.getMessage());
        } finally {
            inputManager.popScanner();
            scriptStack.remove(path);
            if (scriptStack.isEmpty()) {
                stopFlag = false;
            }
        }
    }
}