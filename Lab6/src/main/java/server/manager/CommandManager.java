package server.manager;

import common.Request;
import common.Response;
import server.commands.Command;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Менеджер команд. Является связующим звеном между сетевым слоем и бизнес-логикой.
 */
public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    // Используем синхронизированный список для истории,
    // так как к нему могут обращаться разные потоки (сеть и консоль сервера).
    private final List<String> history = Collections.synchronizedList(new LinkedList<>());
    private final int HISTORY_LIMIT = 13; // Обычно в лабах просят 7-13 элементов

    /**
     * Регистрирует команду в системе.
     */
    public void register(String name, Command command) {
        commands.put(name, command);
    }

    /**
     * Выполняет команду на основе запроса клиента.
     * @param request объект запроса
     * @return объект ответа
     */
    public Response execute(Request request) {
        String commandName = request.getCommandName();
        Command command = commands.get(commandName);

        if (command == null) {
            return new Response("Ошибка: Команда '" + commandName + "' не найдена. Введите 'help'.");
        }

        // Логируем в историю только успешно найденные команды
        addToHistory(commandName);

        try {
            // Выполняем команду. Весь объект Request передается внутрь,
            // чтобы команда могла сама извлечь аргумент или объект Dragon.
            return command.execute(request);
        } catch (Exception e) {
            // Сервер должен "выжить" при любой ошибке внутри команды
            return new Response("Критическая ошибка выполнения команды на сервере: " + e.getMessage());
        }
    }

    /**
     * Добавление в историю с соблюдением лимита.
     */
    private void addToHistory(String name) {
        history.add(name);
        if (history.size() > HISTORY_LIMIT) {
            history.remove(0);
        }
    }

    /**
     * Возвращает историю команд в виде строки с использованием Stream API.
     */
    public String getHistoryString() {
        if (history.isEmpty()) return "История команд пуста.";

        return "Последние " + history.size() + " команд:\n" +
                history.stream()
                        .map(s -> "- " + s)
                        .collect(Collectors.joining("\n"));
    }

    /**
     * Возвращает список всех зарегистрированных команд (для команды help).
     */
    public Map<String, Command> getCommands() {
        return Collections.unmodifiableMap(commands);
    }
}