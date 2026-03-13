import commands.*;
import manager.CollectionManager;
import manager.CommandManager;
import manager.FileManager;
import manager.InputManager;
import java.io.File;
import java.util.Scanner;
/** Главный класс приложения. Реализована загрузка имени файла через переменную окружения DRAGON_FILE с возможностью отката на значение по умолчанию */
public class Main {
    /** Точка входа в программу
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        String envVariable = "DRAGON_FILE";
        String fileName = System.getenv(envVariable);
        if (fileName == null || fileName.trim().isEmpty()) {
            System.out.println("Подсказка: Переменная окружения " + envVariable + " не задана.");
            fileName = "Dragon.xml";
            System.out.println("Использую файл по умолчанию: " + fileName);
        } else {
            System.out.println("Файл загрузки получен из переменной " + envVariable + ": " + fileName);
        }
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Внимание: Файл " + fileName + " пока не существует. Он будет создан при сохранении.");
        }
        FileManager fileManager = new FileManager(fileName);
        CollectionManager collectionManager = new CollectionManager();
        CommandManager commandManager = new CommandManager();
        InputManager inputManager = new InputManager(new Scanner(System.in));
        try {
            collectionManager.getCollection().putAll(fileManager.load());
            System.out.println("Коллекция успешно загружена (элементов: " + collectionManager.getCollection().size() + ").");
        } catch (Exception e) {
            System.out.println("Ошибка при загрузке данных: " + e.getMessage());
        }
        commandManager.register("help", new HelpCommand(commandManager));
        commandManager.register("info", new InfoCommand(collectionManager));
        commandManager.register("show", new ShowCommand(collectionManager));
        commandManager.register("insert", new InsertCommand(collectionManager, inputManager));
        commandManager.register("update", new UpdateCommand(collectionManager, inputManager));
        commandManager.register("remove_key", new RemoveKeyCommand(collectionManager));
        commandManager.register("clear", new ClearCommand(collectionManager));
        commandManager.register("save", new SaveCommand(collectionManager, fileManager));
        commandManager.register("history", new HistoryCommand(commandManager));
        commandManager.register("exit", new ExitCommand());
        commandManager.register("execute_script", new ExecuteScriptCommand(commandManager));
        commandManager.register("remove_greater", new RemoveGreaterCommand(collectionManager, inputManager));
        commandManager.register("replace_if_greater", new ReplaceIfGreaterCommand(collectionManager, inputManager));
        commandManager.register("count_by_weight", new CountByWeightCommand(collectionManager));
        commandManager.register("print_field_descending_character", new PrintFieldDescendingCharacterCommand(collectionManager));
        System.out.println("Программа готова к работе. Введите 'help' для списка команд.");
        while (true) {
            System.out.print("> ");
            String line = inputManager.readLine();
            if (line == null) {
                System.out.println("\nЗавершение работы (обнаружен конец ввода).");
                break;
            }
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(" ", 2);
            String commandName = parts[0];
            String argument = parts.length > 1 ? parts[1] : null;
            commandManager.execute(commandName, argument);
        }
    }
}