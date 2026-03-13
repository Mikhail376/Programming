package commands;
import manager.CommandManager;
import java.util.Map;
/** Команда help: выводит список всех доступных команд с описанием */
public class HelpCommand implements Command {
    /** Менеджер команд, используемый для получения списка команд */
    private final CommandManager commandManager;
    /** Создаёт команду help с указанным менеджером команд
     * @param commandManager менеджер команд
     */
    public HelpCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    /** Выполняет команду, выводя список команд и их описания
     * @param argument аргумент команды (не используется)
     */
    @Override
    public void execute(String argument) {
        System.out.println("Список доступных команд:");
        Map<String, Command> commands = commandManager.getCommands();
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().getDescription());
        }
    }
    /** @return описание команды */
    @Override
    public String getDescription() {
        return "вывести справку по доступным командам";
    }
}