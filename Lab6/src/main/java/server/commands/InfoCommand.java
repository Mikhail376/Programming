package server.commands;

import common.Request;
import common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;

import java.time.format.DateTimeFormatter;

/**
 * Команда info: выводит техническую информацию о коллекции.
 */
public class InfoCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(InfoCommand.class);
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "вывести информацию о коллекции (тип, дата инициализации, количество элементов)";
    }

    @Override
    public Response execute(Request request) {
        logger.info("Запрос информации о коллекции.");

        String dateStr = (collectionManager.getCreationDate() != null)
                ? collectionManager.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))
                : "не определена";

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" > %-20s : %s\n", "Тип", collectionManager.getCollection().getClass().getSimpleName()));
        sb.append(String.format(" > %-20s : %s\n", "Дата инициализации", dateStr));
        sb.append(String.format(" > %-20s : %d\n", "Количество элементов", collectionManager.getCollection().size()));

        return new Response(sb.toString());
    }
}