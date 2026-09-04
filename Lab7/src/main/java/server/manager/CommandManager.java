package server.manager;

import common.Request;
import common.Response;
import server.commands.Command;
import java.util.*;
import java.util.stream.Collectors;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    private final List<String> history = Collections.synchronizedList(new LinkedList<>());
    private final int HISTORY_LIMIT = 5;

    public void register(String name, Command command) {
        commands.put(name, command);
    }

    public Response execute(Request request) {
        String commandName = request.getCommandName();
        Command command = commands.get(commandName);

        if (command == null) {
            return new Response("Ошибка: Команда '" + commandName + "' не найдена. Введите 'help'.");
        }

        addToHistory(commandName);

        try {
            return command.execute(request);
        } catch (Exception e) {
            return new Response("Критическая ошибка выполнения команды на сервере: " + e.getMessage());
        }
    }

    private void addToHistory(String name) {
        history.add(name);
        if (history.size() > HISTORY_LIMIT) {
            history.remove(0);
        }
    }

    public String getHistoryString() {
        if (history.isEmpty()) return "История команд пуста.";

        return "Последние " + history.size() + " команд:\n" +
                history.stream()
                        .map(s -> "- " + s)
                        .collect(Collectors.joining("\n"));
    }

    public Map<String, Command> getCommands() {
        return Collections.unmodifiableMap(commands);
    }
}