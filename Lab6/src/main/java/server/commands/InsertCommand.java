package server.commands;

import common.Request;
import common.Response;
import common.model.Dragon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;

import java.time.LocalDateTime;

/**
 * Команда insert: добавляет новый элемент в коллекцию.
 * Реализует серверную валидацию и генерацию технических полей (ID, дата).
 */
public class InsertCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(InsertCommand.class);
    private final CollectionManager collectionManager;

    public InsertCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "insert";
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент с заданным ключом";
    }

    @Override
    public Response execute(Request request) {
        String argument = request.getCommandArgument();
        Dragon dragon = request.getObjectArgument();

        if (argument == null || argument.isBlank()) {
            logger.warn("Команда insert вызвана без ключа.");
            return new Response("Ошибка: не указан ключ для вставки.");
        }

        if (dragon == null) {
            logger.warn("Команда insert вызвана без объекта Dragon.");
            return new Response("Ошибка: объект Dragon не передан.");
        }

        try {
            long key = Long.parseLong(argument);

            if (collectionManager.getCollection().containsKey(key)) {
                logger.warn("Попытка вставки по существующему ключу: {}", key);
                return new Response("Ошибка: элемент с ключом " + key + " уже существует. Используйте 'update' для изменения.");
            }

            dragon.setId(collectionManager.generateNextId());
            dragon.setCreationDate(LocalDateTime.now());

            collectionManager.insert(key, dragon);

            logger.info("Добавлен новый дракон: ключ {}, ID {}, имя '{}'.",
                    key, dragon.getId(), dragon.getName());

            return new Response("Дракон '" + dragon.getName() + "' успешно добавлен с ID " + dragon.getId());

        } catch (NumberFormatException e) {
            logger.error("Некорректный формат ключа: {}", argument);
            return new Response("Ошибка: ключ должен быть целым числом (Long).");
        } catch (Exception e) {
            logger.error("Ошибка при выполнении insert: {}", e.getMessage());
            return new Response("Произошла внутренняя ошибка сервера при добавлении элемента.");
        }
    }
}