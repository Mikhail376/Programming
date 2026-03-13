package commands;
import manager.CommandManager;
import java.io.*;
import java.util.HashSet;
import java.util.Set;
/** Команда execute_script: выполняет команды из указанного файла скрипта, предотвращая рекурсию */
public class ExecuteScriptCommand implements Command {
    /** Менеджер команд, используемый для выполнения команд из скрипта */
    private final CommandManager commandManager;
    /** Стек файлов скриптов для предотвращения бесконечной рекурсии */
    private static final Set<String> scriptStack = new HashSet<>();
    /** Создаёт команду execute_script с указанным менеджером команд
     * @param commandManager менеджер команд
     */
    public ExecuteScriptCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    /** Выполняет команды из файла скрипта
     * @param argument путь к файлу скрипта
     */
    @Override
    public void execute(String argument) {
        if (argument == null || argument.isEmpty()) {
            System.out.println("Ошибка: не указано имя файла скрипта.");
            return;
        }
        File file = new File(argument);
        if (!file.exists()) {
            System.out.println("Ошибка: файл скрипта не найден.");
            return;
        }
        if (scriptStack.contains(file.getAbsolutePath())) {
            System.out.println("Ошибка: Обнаружена бесконечная рекурсия в скрипте!");
            return;
        }
        scriptStack.add(file.getAbsolutePath());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(" ", 2);
                String cmd = parts[0];
                String arg = parts.length > 1 ? parts[1] : null;
                System.out.println("Исполнение: " + cmd);
                commandManager.execute(cmd, arg);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении скрипта: " + e.getMessage());
        } finally {
            scriptStack.remove(file.getAbsolutePath());
        }
    }
    /** @return описание команды */
    @Override
    public String getDescription() {
        return "считать и исполнить скрипт из указанного файла";
    }
}