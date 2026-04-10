package server.commands;

import common.Request;
import common.Response;
import common.model.Dragon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Команда print_field_descending_character: выводит значения поля character
 * всех элементов коллекции в порядке убывания.
 */
public class PrintFieldDescendingCharacterCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(PrintFieldDescendingCharacterCommand.class);
    private final CollectionManager collectionManager;

    public PrintFieldDescendingCharacterCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "print_field_descending_character";
    }

    @Override
    public String getDescription() {
        return "вывести значения поля character всех элементов в порядке убывания";
    }

    @Override
    public Response execute(Request request) {
        logger.info("Выполнение команды вывода характеров в порядке убывания.");

        if (collectionManager.getCollection().isEmpty()) {
            logger.info("Коллекция пуста, выводить нечего.");
            return new Response("Коллекция пуста.");
        }

        String result = collectionManager.getCollection().values().stream()
                .map(Dragon::getCharacter)
                .filter(Objects::nonNull)
                .sorted(Comparator.reverseOrder())
                .map(Enum::toString)
                .collect(Collectors.joining("\n"));

        if (result.isEmpty()) {
            logger.warn("Ни у одного дракона не заполнено поле character.");
            return new Response("У элементов коллекции не задано поле character.");
        }

        logger.info("Список характеров успешно сформирован.");
        return new Response("Значения поля character в порядке убывания:\n" + result);
    }
}