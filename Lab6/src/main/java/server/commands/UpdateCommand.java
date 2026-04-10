package server.commands;

import common.Request;
import common.Response;
import common.model.Dragon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;

import java.util.Map;
import java.util.Optional;

/**
 * Команда update: обновляет данные элемента коллекции по его ID.
 */
public class UpdateCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(UpdateCommand.class);
    private final CollectionManager collectionManager;

    public UpdateCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }

    @Override
    public Response execute(Request request) {
        String argument = request.getCommandArgument();
        Dragon updatedDragon = request.getObjectArgument();

        if (argument == null || argument.isBlank()) {
            logger.warn("Команда update вызвана без ID.");
            return new Response("Ошибка: не указан ID для обновления.");
        }

        if (updatedDragon == null) {
            logger.warn("Команда update: не получены данные объекта.");
            return new Response("Ошибка: объект для обновления не передан.");
        }

        try {
            long id = Long.parseLong(argument);

            if (collectionManager.getCollection().isEmpty()) {
                return new Response("Коллекция пуста, обновлять нечего.");
            }

            Optional<Long> keyToUpdate = collectionManager.getCollection().entrySet().stream()
                    .filter(entry -> entry.getValue().getId() == id)
                    .map(Map.Entry::getKey)
                    .findFirst();

            if (keyToUpdate.isEmpty()) {
                logger.info("Объект с ID {} не найден.", id);
                return new Response("Ошибка: в коллекции нет элемента с ID " + id);
            }

            Long key = keyToUpdate.get();
            Dragon oldDragon = collectionManager.getCollection().get(key);

            updatedDragon.setId(oldDragon.getId());
            updatedDragon.setCreationDate(oldDragon.getCreationDate());

            collectionManager.insert(key, updatedDragon);

            logger.info("Объект с ID {} (ключ {}) успешно обновлен.", id, key);
            return new Response("Элемент с ID " + id + " успешно обновлён.");

        } catch (NumberFormatException e) {
            logger.error("Некорректный формат ID: {}", argument);
            return new Response("Ошибка: ID должен быть целым числом.");
        }
    }
}