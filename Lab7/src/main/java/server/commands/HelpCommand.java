package server.commands;
import common.Request;
import common.Response;
import server.manager.CommandManager;
import java.util.stream.Collectors;

public class HelpCommand implements Command {
    private final CommandManager commandManager;
    public HelpCommand(CommandManager commandManager) { this.commandManager = commandManager; }
    @Override public String getName() { return "help"; }
    @Override public String getDescription() { return "справка по командам"; }
    @Override public Response execute(Request request) {
        return new Response(commandManager.getCommands().entrySet().stream()
                .sorted(java.util.Map.Entry.comparingByKey())
                .map(e -> String.format(" > %-25s : %s", e.getKey(), e.getValue().getDescription()))
                .collect(Collectors.joining("\n")));
    }
}