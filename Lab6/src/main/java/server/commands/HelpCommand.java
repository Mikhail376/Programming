package server.commands;

import common.Request;
import common.Response;
import server.manager.CommandManager;

import java.util.stream.Collectors;

/**
 * Команда help: выводит справку по всем доступным командам.
 */
public class HelpCommand implements Command {
    private final CommandManager commandManager;

    public HelpCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "вывести справку по доступным командам";
    }

    @Override
    public Response execute(Request request) {
        String helpText = "Доступные команды:\n" +
                commandManager.getCommands().entrySet().stream()
                        .sorted(java.util.Map.Entry.comparingByKey())
                        .map(entry -> String.format(" > %-15s : %s",
                                entry.getKey(),
                                entry.getValue().getDescription()))
                        .collect(Collectors.joining("\n"));

        String fullHelp = helpText + "\n" +
                String.format(" > %-15s : %s", "execute_script", "исполнить скрипт из указанного файла") + "\n" +
                String.format(" > %-15s : %s", "exit", "завершить работу клиента");

        return new Response(fullHelp);
    }
}