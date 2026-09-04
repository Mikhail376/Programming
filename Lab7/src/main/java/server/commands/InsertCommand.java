package server.commands;
import common.Request;
import common.Response;
import common.model.Dragon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;

public class InsertCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(InsertCommand.class);
    private final CollectionManager cm;
    public InsertCommand(CollectionManager cm) { this.cm = cm; }

    @Override public String getName() { return "insert"; }
    @Override public String getDescription() { return "добавить новый элемент с заданным ключом"; }

    @Override public Response execute(Request request) {
        String arg = request.getCommandArgument();
        Dragon dragon = request.getObjectArgument();
        String owner = request.getUsername();
        if (request.getObjectArgument() == null) {
            return new Response("OK");
        }
        if (arg == null || arg.isBlank()) return new Response("Ошибка: не указан ключ.");
        if (dragon == null) return new Response("Ошибка: объект не передан.");

        try {
            long key = Long.parseLong(arg);
            if (cm.containsKey(key)) return new Response("Ошибка: элемент с ключом " + key + " уже существует.");

            dragon.setOwner(owner);

            if (cm.insert(key, dragon)) {
                logger.info("Добавлен дракон '{}' (ключ: {}, ID: {}, владелец: {})", dragon.getName(), key, dragon.getId(), owner);
                return new Response("Дракон успешно добавлен (ID: " + dragon.getId() + ")");
            } else {
                return new Response("Ошибка БД при сохранении.");
            }
        } catch (NumberFormatException e) { return new Response("Ошибка: ключ должен быть целым числом."); }
        catch (Exception e) { return new Response("Внутренняя ошибка: " + e.getMessage()); }
    }
}