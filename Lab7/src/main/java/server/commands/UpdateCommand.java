package server.commands;
import common.Request;
import common.Response;
import common.model.Dragon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;
import java.util.Map;
import java.util.Optional;

public class UpdateCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(UpdateCommand.class);
    private final CollectionManager cm;
    public UpdateCommand(CollectionManager cm) { this.cm = cm; }

    @Override public String getName() { return "update"; }
    @Override public String getDescription() { return "обновить элемент коллекции по его ID"; }

    @Override public Response execute(Request request) {
        String arg = request.getCommandArgument();
        Dragon updated = request.getObjectArgument();
        String owner = request.getUsername();
        if (request.getObjectArgument() == null) {
            return new Response("OK");
        }
        if (arg == null || updated == null) return new Response("Ошибка: укажите ID и объект.");
        try {
            long id = Long.parseLong(arg);
            Optional<Long> keyOpt = cm.getCollectionMap().entrySet().stream()
                    .filter(e -> e.getValue().getId() == id)
                    .map(Map.Entry::getKey).findFirst();

            if (keyOpt.isEmpty()) return new Response("Элемент с ID " + id + " не найден.");
            Long key = keyOpt.get();
            Dragon old = cm.get(key);
            if (!old.getOwner().equals(owner)) return new Response("Ошибка: нет прав на изменение этого объекта.");

            updated.setId(id);
            updated.setCreationDate(old.getCreationDate());

            return cm.update(key, updated)
                    ? new Response("Элемент с ID " + id + " успешно обновлён.")
                    : new Response("Ошибка БД при обновлении.");
        } catch (NumberFormatException e) { return new Response("Ошибка формата ID."); }
        catch (Exception e) { return new Response("Внутренняя ошибка: " + e.getMessage()); }
    }
}