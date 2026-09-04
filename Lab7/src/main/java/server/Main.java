package server;

import server.manager.*;
import server.commands.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try { port = Integer.parseInt(args[0]); }
            catch (NumberFormatException e) { logger.warn("Некорректный порт. Используется: {}", DEFAULT_PORT); }
        }

        String jdbcUrl = "jdbc:postgresql://localhost:5433/studs";
        String dbUser = System.getenv("s503264");
        String dbPass = System.getenv("HzTIHoqxFbCHY");


        if (dbUser == null) dbUser = "s503264";
        if (dbPass == null) dbPass = "HzTIHoqxFbCHY";

        try {

            DatabaseManager dbManager = new DatabaseManager(jdbcUrl, dbUser, dbPass);
            AuthManager authManager = new AuthManager(jdbcUrl, dbUser, dbPass);
            CollectionManager collectionManager = new CollectionManager(dbManager);


            collectionManager.loadFromDB();
            logger.info("Коллекция загружена из БД. Элементов: {}", collectionManager.size());

            CommandManager commandManager = new CommandManager();
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
            commandManager.register("register", new RegisterCommand(authManager));

            Server server = new Server(port, commandManager, authManager, collectionManager);
            server.run();
        } catch (Exception e) {
            logger.error("Критическая ошибка инициализации: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}