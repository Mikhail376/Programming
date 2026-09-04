package server.commands;
import common.Request;
import common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;

public class ShowCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(ShowCommand.class);
    private final CollectionManager cm;
    public ShowCommand(CollectionManager cm) { this.cm = cm; }

    @Override public String getName() { return "show"; }
    @Override public String getDescription() { return "вывести все элементы коллекции"; }

    @Override public Response execute(Request request) {
        var map = cm.getCollectionMap();
        if (map.isEmpty()) return new Response("Коллекция пуста.");
        StringBuilder sb = new StringBuilder("=== Коллекция (" + map.size() + " эл.) ===\n");
        map.forEach((k, v) -> sb.append("Ключ: ").append(k).append(" | ").append(v).append("\n"));
        return new Response(sb.toString());
    }
}