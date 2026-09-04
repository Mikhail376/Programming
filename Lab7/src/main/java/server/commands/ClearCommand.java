package server.commands;
import common.Request;
import common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;

public class ClearCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(ClearCommand.class);
    private final CollectionManager cm;
    public ClearCommand(CollectionManager cm) { this.cm = cm; }

    @Override public String getName() { return "clear"; }
    @Override public String getDescription() { return "очистить коллекцию (только ваши объекты)"; }

    @Override public Response execute(Request request) {
        String owner = request.getUsername();
        try {
            cm.clearByOwner(owner);
            logger.info("Пользователь {} очистил свои объекты.", owner);
            return new Response("Коллекция очищена (удалены ваши объекты).");
        } catch (Exception e) { return new Response("Ошибка очистки: " + e.getMessage()); }
    }
}

