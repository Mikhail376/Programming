package server.commands;

import common.Request;
import common.Response;
import common.model.Dragon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;

/**
 * Команда replace_if_greater: заменяет значение по ключу, если новый объект "больше" старого.
 */
public class ReplaceIfGreaterCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(ReplaceIfGreaterCommand.class);
    private final CollectionManager collectionManager;

    public ReplaceIfGreaterCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "replace_if_greater";
    }

    @Override
    public String getDescription() {
        return "заменить значение по ключу, если новое значение больше старого";
    }

    @Override
    public Response execute(Request request) {
        String argument = request.getCommandArgument();
        Dragon newDragon = request.getObjectArgument();

        if (argument == null || argument.isBlank()) {
            logger.warn("Команда replace_if_greater вызвана без ключа.");
            return new Response("Ошибка: не указан ключ для замены.");
        }

        if (newDragon == null) {
            logger.warn("Команда replace_if_greater вызвана без объекта Dragon.");
            return new Response("Ошибка: объект для сравнения не передан.");
        }

        try {
            long key = Long.parseLong(argument);

            Dragon oldDragon = collectionManager.getCollection().get(key);

            if (oldDragon == null) {
                logger.info("Попытка замены для несуществующего ключа: {}.", key);
                return new Response("Ошибка: элемент с ключом " + key + " не найден в коллекции.");
            }

            if (newDragon.compareTo(oldDragon) > 0) {
                newDragon.setId(oldDragon.getId());
                newDragon.setCreationDate(oldDragon.getCreationDate());

                collectionManager.insert(key, newDragon);

                logger.info("Элемент с ключом {} успешно заменен (новый больше старого).", key);
                return new Response("Успешно: элемент с ключом " + key + " заменен.");
            } else {
                logger.info("Замена по ключу {} отклонена: новое значение <= старому.", key);
                return new Response("Замена не произведена: характеристики нового дракона не превышают старого.");
            }

        } catch (NumberFormatException e) {
            logger.error("Некорректный формат ключа: {}", argument);
            return new Response("Ошибка: ключ должен быть целым числом.");
        }
    }
}