package server.commands;

import common.Request;
import common.Response;
import common.model.Dragon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;

/**
 * Команда remove_greater: удаляет элементы, которые больше заданного.
 */
public class RemoveGreaterCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(RemoveGreaterCommand.class);
    private final CollectionManager collectionManager;

    public RemoveGreaterCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "remove_greater";
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, превышающие заданный";
    }

    @Override
    public Response execute(Request request) {
        Dragon referenceDragon = request.getObjectArgument();

        if (referenceDragon == null) {
            logger.warn("Команда remove_greater вызвана без объекта Dragon.");
            return new Response("Ошибка: объект для сравнения не передан.");
        }

        if (collectionManager.getCollection().isEmpty()) {
            return new Response("Коллекция пуста, сравнивать не с чем.");
        }

        int removedCount = collectionManager.removeGreater(referenceDragon);

        if (removedCount == 0) {
            logger.info("Выполнена remove_greater: подходящих элементов не найдено.");
            return new Response("Элементов, превышающих заданный, не обнаружено.");
        }

        logger.info("Успешно удалено элементов: {}", removedCount);
        return new Response("Команда успешно выполнена. Удалено элементов: " + removedCount);
    }
}