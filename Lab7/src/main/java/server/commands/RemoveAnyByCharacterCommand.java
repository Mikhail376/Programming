package server.commands;
import common.Request;
import common.Response;
import common.model.DragonCharacter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;
import java.util.Map;
import java.util.Optional;

public class RemoveAnyByCharacterCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(RemoveAnyByCharacterCommand.class);
    private final CollectionManager cm;
    public RemoveAnyByCharacterCommand(CollectionManager cm) { this.cm = cm; }

    @Override public String getName() { return "remove_any_by_character"; }
    @Override public String getDescription() { return "удалить один элемент с заданным характером (только ваш)"; }

    @Override public Response execute(Request request) {
        String arg = request.getCommandArgument();
        String owner = request.getUsername();
        if (arg == null) return new Response("Ошибка: не указан характер.");
        try {
            DragonCharacter ch = DragonCharacter.valueOf(arg.trim().toUpperCase());
            Optional<Long> keyOpt = cm.getCollectionMap().entrySet().stream()
                    .filter(e -> e.getValue().getCharacter() == ch)
                    .filter(e -> e.getValue().getOwner().equals(owner))
                    .map(Map.Entry::getKey).findFirst();

            if (keyOpt.isEmpty()) return new Response("Ваших драконов с характером " + ch + " не найдено.");
            return cm.remove(keyOpt.get())
                    ? new Response("Успешно: удалён один элемент с характером " + ch)
                    : new Response("Ошибка БД при удалении.");
        } catch (IllegalArgumentException e) { return new Response("Недопустимый характер. Доступные: CUNNING, WISE, EVIL, GOOD"); }
        catch (Exception e) { return new Response("Внутренняя ошибка: " + e.getMessage()); }
    }
}