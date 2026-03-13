package commands;
import manager.CollectionManager;
/** Команда clear: очищает всю коллекцию */
public class ClearCommand implements Command {
    /** Менеджер коллекции, с которым работает команда */
    private final CollectionManager collectionManager;
    /** Создаёт команду clear с указанным менеджером коллекции
     * @param collectionManager менеджер коллекции
     */
    public ClearCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    /** Выполняет команду очистки коллекции
     * @param argument аргумент команды (не используется)
     */
    @Override
    public void execute(String argument) {
        collectionManager.clear();
        System.out.println("Коллекция очищена.");
    }
    /** @return описание команды */
    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }
}