package server.commands;
import common.Request;
import common.Response;
import common.model.Dragon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;

public class RemoveKeyCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(RemoveKeyCommand.class);
    private final CollectionManager cm;
    public RemoveKeyCommand(CollectionManager cm) { this.cm = cm; }

    @Override public String getName() { return "remove_key"; }
    @Override public String getDescription() { return "удалить элемент по ключу"; }

    @Override public Response execute(Request request) {
        String arg = request.getCommandArgument();
        String owner = request.getUsername();
        if (arg == null) return new Response("Ошибка: не указан ключ.");
        try {
            long key = Long.parseLong(arg);
            Dragon d = cm.get(key);
            if (d == null) return new Response("Ключ не найден.");
            if (!d.getOwner().equals(owner)) return new Response("Ошибка: нет прав на удаление.");

            return cm.remove(key) ? new Response("Элемент удалён.") : new Response("Ошибка БД при удалении.");
        } catch (NumberFormatException e) { return new Response("Ошибка формата ключа."); }
        catch (Exception e) { return new Response("Внутренняя ошибка: " + e.getMessage()); }
    }
}