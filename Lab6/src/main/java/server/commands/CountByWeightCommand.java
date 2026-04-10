package server.commands;

import common.Request;
import common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;

/**
 * Команда count_by_weight: выводит количество элементов с заданным весом.
 */
public class CountByWeightCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(CountByWeightCommand.class);
    private final CollectionManager collectionManager;

    public CountByWeightCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "count_by_weight";
    }

    @Override
    public String getDescription() {
        return "вывести количество элементов с заданным весом";
    }

    @Override
    public Response execute(Request request) {
        String argument = request.getCommandArgument();

        if (argument == null || argument.isBlank()) {
            logger.warn("Команда count_by_weight вызвана без аргумента.");
            return new Response("Ошибка: не указан вес для сравнения.");
        }

        try {
            float weight = Float.parseFloat(argument);   // ← исправлено под модель

            long count = collectionManager.countByWeight(weight);

            logger.info("Выполнена команда count_by_weight для веса {}. Результат: {}.", weight, count);

            return new Response("Количество драконов с весом " + weight + ": " + count);

        } catch (NumberFormatException e) {
            logger.error("Ошибка парсинга веса: '{}' не является числом.", argument);
            return new Response("Ошибка: аргумент '" + argument + "' должен быть числом.");
        }
    }
}