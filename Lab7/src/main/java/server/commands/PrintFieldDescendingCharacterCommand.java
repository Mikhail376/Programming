package server.commands;
import common.Request;
import common.Response;
import server.manager.CollectionManager;
import java.util.Comparator;
import java.util.stream.Collectors;

public class PrintFieldDescendingCharacterCommand implements Command {
    private final CollectionManager cm;
    public PrintFieldDescendingCharacterCommand(CollectionManager cm) { this.cm = cm; }

    @Override public String getName() { return "print_field_descending_character"; }
    @Override public String getDescription() { return "значения character в порядке убывания"; }

    @Override public Response execute(Request request) {
        if (cm.getAll().isEmpty()) return new Response("Коллекция пуста.");
        String res = cm.getAll().stream()
                .map(d -> d.getCharacter().toString())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.joining("\n"));
        return new Response(res);
    }
}