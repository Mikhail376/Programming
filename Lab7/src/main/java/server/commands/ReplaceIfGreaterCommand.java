package server.commands;
import common.Request;
import common.Response;
import common.model.Dragon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.CollectionManager;

public class ReplaceIfGreaterCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(ReplaceIfGreaterCommand.class);
    private final CollectionManager cm;
    public ReplaceIfGreaterCommand(CollectionManager cm) { this.cm = cm; }

    @Override public String getName() { return "replace_if_greater"; }
    @Override public String getDescription() { return "заменить значение, если новое больше старого (только ваше)"; }

    @Override public Response execute(Request request) {
        String arg = request.getCommandArgument();
        Dragon newD = request.getObjectArgument();
        String owner = request.getUsername();
        if (request.getObjectArgument() == null) {
            return new Response("OK");
        }
        if (arg == null || newD == null) return new Response("Ошибка: укажите ключ и объект.");
        try {
            long key = Long.parseLong(arg);
            Dragon oldD = cm.get(key);
            if (oldD == null) return new Response("Ключ не найден.");
            if (!oldD.getOwner().equals(owner)) return new Response("Ошибка: нет прав на изменение.");

            if (newD.compareTo(oldD) > 0) {
                newD.setId(oldD.getId());
                newD.setCreationDate(oldD.getCreationDate());
                return cm.update(key, newD) ? new Response("Элемент успешно заменён.") : new Response("Ошибка БД.");
            } else {
                return new Response("Замена не произведена: характеристики нового дракона не превышают старого.");
            }
        } catch (NumberFormatException e) { return new Response("Ошибка формата ключа."); }
        catch (Exception e) { return new Response("Внутренняя ошибка: " + e.getMessage()); }
    }
}