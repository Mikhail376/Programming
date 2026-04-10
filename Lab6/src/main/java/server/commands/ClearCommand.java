package server.commands;
import common.Request;
import common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;

/**
 * Команда clear, очищает коллекцию.
 */

public class ClearCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(ClearCommand.class);
    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }

    @Override
    public Response execute(Request request) {
        int sizeBefore = collectionManager.getCollection().size();

        if (sizeBefore == 0) {
            return new Response("Коллекция уже пуста.");
        }

        collectionManager.clear();
        logger.info("Коллекция очищена. Удалено элементов: {}", sizeBefore);

        return new Response("Коллекция успешно очищена. Удалено элементов: " + sizeBefore);
    }
}

