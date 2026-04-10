package server.commands;

import common.Request;
import common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;
import server.manager.FileManager;

/**
 * Команда save: сохраняет коллекцию в файл.
 * Доступна только для локального вызова из консоли сервера.
 */
public class SaveCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(SaveCommand.class);
    private final CollectionManager collectionManager;
    private final FileManager fileManager;

    public SaveCommand(CollectionManager collectionManager, FileManager fileManager) {
        this.collectionManager = collectionManager;
        this.fileManager = fileManager;
    }

    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String getDescription() {
        return "сохранить коллекцию в файл (доступно только консоли сервера)";
    }

    @Override
    public Response execute(Request request) {
        logger.warn("Попытка удаленного вызова команды 'save' отклонена.");
        return new Response("Ошибка: команда 'save' выполняется только локально на сервере.");
    }

    public void serverExecute() {
        try {
            logger.info("Начало процесса сохранения коллекции...");
            fileManager.save(collectionManager.getCollection());
            logger.info("Коллекция успешно сохранена. Элементов обработано: {}",
                    collectionManager.getCollection().size());
        } catch (Exception e) {
            logger.error("Критическая ошибка при сохранении данных: {}", e.getMessage());
        }
    }
}