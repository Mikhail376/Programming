package server;

import server.manager.*;
import server.commands.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Главный класс сервера.
 * Отвечает за инициализацию менеджеров, регистрацию команд и запуск сетевого модуля.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                logger.warn("Некорректный порт. Используется порт по умолчанию: {}", port);
            }
        }

        String fileName = System.getenv("DRAGON_FILE");
        if (fileName == null || fileName.isEmpty()) {
            fileName = "dragons.xml";
            logger.warn("Переменная среды DRAGON_FILE не задана. Используется: {}", fileName);
        }

        FileManager fileManager = new FileManager(fileName);
        CollectionManager collectionManager = new CollectionManager();
        CommandManager commandManager = new CommandManager();
        collectionManager.loadCollection(fileManager);

        commandManager.register("help", new HelpCommand(commandManager));
        commandManager.register("info", new InfoCommand(collectionManager));
        commandManager.register("show", new ShowCommand(collectionManager));
        commandManager.register("insert", new InsertCommand(collectionManager));
        commandManager.register("update", new UpdateCommand(collectionManager));
        commandManager.register("remove_key", new RemoveKeyCommand(collectionManager));
        commandManager.register("clear", new ClearCommand(collectionManager));
        commandManager.register("remove_greater", new RemoveGreaterCommand(collectionManager));
        commandManager.register("replace_if_greater", new ReplaceIfGreaterCommand(collectionManager));
        commandManager.register("remove_any_by_character", new RemoveAnyByCharacterCommand(collectionManager));
        commandManager.register("count_by_weight", new CountByWeightCommand(collectionManager));
        commandManager.register("print_field_descending_character", new PrintFieldDescendingCharacterCommand(collectionManager));

        commandManager.register("save", new SaveCommand(collectionManager, fileManager));

        logger.info("Инициализация сетевого модуля...");
        Server server = new Server(port, commandManager);
        server.run();
    }
}