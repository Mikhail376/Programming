package server.commands;

import common.Request;
import common.Response;
import common.model.Dragon;
import common.model.DragonCharacter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;

import java.util.Optional;

/**
 * Команда remove_any_by_character: удаляет из коллекции ОДИН элемент,
 * значение поля character которого эквивалентно заданному.
 */
public class RemoveAnyByCharacterCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(RemoveAnyByCharacterCommand.class);
    private final CollectionManager collectionManager;

    public RemoveAnyByCharacterCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "remove_any_by_character";
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции один элемент, значение поля character которого эквивалентно заданному";
    }

    @Override
    public Response execute(Request request) {
        String argument = request.getCommandArgument();

        if (argument == null || argument.isBlank()) {
            logger.warn("Команда remove_any_by_character вызвана без аргумента.");
            return new Response("Ошибка: не указан характер для удаления.");
        }

        try {
            DragonCharacter character = DragonCharacter.valueOf(argument.trim().toUpperCase());

            Optional<Long> keyToRemove = collectionManager.getCollection().entrySet().stream()
                    .filter(entry -> entry.getValue().getCharacter() == character)
                    .map(java.util.Map.Entry::getKey)
                    .findFirst();   // любой первый (требование "any")

            if (keyToRemove.isEmpty()) {
                logger.info("Элемент с характером {} не найден.", character);
                return new Response("Элемент с характером " + character + " в коллекции не найден.");
            }

            Long key = keyToRemove.get();
            collectionManager.remove(key);

            logger.info("Успешно удалён элемент с ключом {} и характером {}.", key, character);
            return new Response("Успешно: удалён один элемент с характером " + character);

        } catch (IllegalArgumentException e) {
            logger.error("Некорректный характер: {}", argument);
            return new Response("Ошибка: '" + argument + "' не является допустимым характером.\n" +
                    "Доступные: " + java.util.Arrays.toString(DragonCharacter.values()));
        } catch (Exception e) {
            logger.error("Ошибка при выполнении remove_any_by_character: {}", e.getMessage());
            return new Response("Произошла внутренняя ошибка сервера.");
        }
    }
}