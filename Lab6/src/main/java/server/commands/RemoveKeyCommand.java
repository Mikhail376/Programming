package server.commands;

import common.Request;
import common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;

/**
 * Команда remove_key: удаляет элемент из коллекции по его ключу.
 */
public class RemoveKeyCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(RemoveKeyCommand.class);
    private final CollectionManager collectionManager;

    public RemoveKeyCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "remove_key";
    }

    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по его ключу";
    }

    @Override
    public Response execute(Request request) {
        String argument = request.getCommandArgument();

        if (argument == null || argument.isBlank()) {
            logger.warn("Команда remove_key вызвана без аргумента.");
            return new Response("Ошибка: не указан ключ для удаления.");
        }

        try {
            long key = Long.parseLong(argument);

            if (collectionManager.remove(key)) {
                logger.info("Элемент с ключом {} успешно удален.", key);
                return new Response("Элемент с ключом " + key + " успешно удалён.");
            } else {
                logger.info("Попытка удаления несуществующего ключа: {}.", key);
                return new Response("Ошибка: элемент с ключом " + key + " не найден в коллекции.");
            }

        } catch (NumberFormatException e) {
            logger.error("Некорректный формат ключа: {}", argument);
            return new Response("Ошибка: ключ должен быть целым числом.");
        }
    }
}