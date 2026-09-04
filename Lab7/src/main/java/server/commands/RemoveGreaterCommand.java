package server.commands;
import common.Request;
import common.Response;
import common.model.Dragon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;

public class RemoveGreaterCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(RemoveGreaterCommand.class);
    private final CollectionManager cm;
    public RemoveGreaterCommand(CollectionManager cm) { this.cm = cm; }

    @Override public String getName() { return "remove_greater"; }
    @Override public String getDescription() { return "удалить все элементы, превышающие заданный (только ваши)"; }

    @Override public Response execute(Request request) {
        Dragon ref = request.getObjectArgument();
        String owner = request.getUsername();
        if (request.getObjectArgument() == null) {
            return new Response("OK");
        }
        if (ref == null) return new Response("Ошибка: объект для сравнения не передан.");
        try {
            int removed = cm.removeGreater(ref, owner);
            return new Response("Удалено ваших элементов: " + removed);
        } catch (Exception e) { return new Response("Внутренняя ошибка: " + e.getMessage()); }
    }
}