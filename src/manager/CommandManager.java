package manager;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import commands.Command;
/**
 * Класс для управления командами и историей их выполнения.
 */
public class CommandManager {
    /** Список зарегистрированных команд. */
    private final Map<String, Command> commands = new HashMap<>();
    /** История выполненных команд. */
    private final LinkedList<String> history = new LinkedList<>();
    /** Максимальное количество команд в истории. */
    private final int HISTORY_LIMIT = 5;
    /**
     * Регистрирует новую команду.
     *
     * @param name имя команды
     * @param command объект команды
     */
    public void register(String name, Command command) {
        commands.put(name, command);
    }
    /**
     * Выполняет команду по её имени.
     *
     * @param name имя команды
     * @param argument аргумент команды
     */
    public void execute(String name, String argument) {
        Command command = commands.get(name);
        if (command == null) {
            System.out.println("Неизвестная команда: " + name);
            return;
        }
        addToHistory(name);
        try {
            command.execute(argument);
        } catch (Exception e) {
            System.out.println("Ошибка выполнения команды: " + e.getMessage());
        }
    }
    /**
     * Добавляет команду в историю.
     */
    private void addToHistory(String name) {
        history.addFirst(name);
        if (history.size() > HISTORY_LIMIT) {
            history.removeLast();
        }
    }
    /**
     * @return история последних выполненных команд
     */
    public LinkedList<String> getHistory() {
        return new LinkedList<>(history);
    }
    /**
     * @return карта зарегистрированных команд
     */
    public Map<String, Command> getCommands() {
        return new HashMap<>(commands);
    }
}