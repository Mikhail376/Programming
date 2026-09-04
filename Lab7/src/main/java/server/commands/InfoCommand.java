package server.commands;
import common.Request;
import common.Response;
import server.manager.CollectionManager;
import java.time.format.DateTimeFormatter;

public class InfoCommand implements Command {
    private final CollectionManager cm;
    public InfoCommand(CollectionManager cm) { this.cm = cm; }

    @Override public String getName() { return "info"; }
    @Override public String getDescription() { return "информация о коллекции"; }

    @Override public Response execute(Request request) {
        String date = cm.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        return new Response("Тип: TreeMap\nДата инициализации: " + date + "\nКоличество элементов: " + cm.size());
    }
}