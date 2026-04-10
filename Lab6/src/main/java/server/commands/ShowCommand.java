package server.commands;

import common.Request;
import common.Response;
import common.model.Dragon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Команда show: передаёт клиенту всю коллекцию в сериализованном виде.
 */
public class ShowCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(ShowCommand.class);
    private final CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescription() {
        return "вывести все элементы коллекции в строковом представлении";
    }

    @Override
    public Response execute(Request request) {
        logger.info("Запрос на отображение всей коллекции.");

        Collection<Dragon> dragons = collectionManager.getCollection().values();

        if (dragons.isEmpty()) {
            logger.info("Коллекция пуста.");
            return new Response("Коллекция в данный момент пуста.");
        }

        logger.info("Коллекция успешно подготовлена для отправки клиенту. Элементов: {}", dragons.size());

        return new Response(
                "=== Содержимое коллекции (" + dragons.size() + " элементов) ===",
                new ArrayList<>(dragons)
        );
    }
}