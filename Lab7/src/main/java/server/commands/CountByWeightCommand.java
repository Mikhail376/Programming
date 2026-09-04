package server.commands;
import common.Request;
import common.Response;
import server.manager.CollectionManager;

public class CountByWeightCommand implements Command {
    private final CollectionManager cm;
    public CountByWeightCommand(CollectionManager cm) { this.cm = cm; }

    @Override public String getName() { return "count_by_weight"; }
    @Override public String getDescription() { return "количество элементов с заданным весом"; }

    @Override public Response execute(Request request) {
        String arg = request.getCommandArgument();
        if (arg == null || arg.isBlank()) return new Response("Ошибка: не указан вес.");
        try {
            float w = Float.parseFloat(arg);
            long count = cm.countByWeight(w);
            return new Response("Драконов с весом " + w + ": " + count);
        } catch (NumberFormatException e) { return new Response("Ошибка: аргумент должен быть числом."); }
    }
}