package commands;
import manager.CommandManager;
import java.util.List;
/** Команда history: выводит последние 5 выполненных команд */
public class HistoryCommand implements Command {
    /** Менеджер команд, используемый для получения истории выполнения */
    private final CommandManager commandManager;
    /** Создаёт команду history с указанным менеджером команд
     * @param commandManager менеджер команд
     */
    public HistoryCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    /** Выполняет команду, выводя последние 5 выполненных команд
     * @param argument аргумент команды (не используется)
     */
    @Override
    public void execute(String argument) {
        List<String> history = commandManager.getHistory();
        if (history.isEmpty()) {
            System.out.println("История команд пуста.");
            return;
        }
        System.out.println("Последние выполненные команды:");
        for (String commandName : history) {
            System.out.println(commandName);
        }
    }
    /** @return описание команды */
    @Override
    public String getDescription() {
        return "вывести последние 5 команд (без аргументов)";
    }
}