package commands;
import manager.CollectionManager;
import model.Dragon;
/** Команда show: выводит все элементы коллекции */
public class ShowCommand implements Command {
    /** Менеджер коллекции, используемый для получения элементов */
    private final CollectionManager collectionManager;
    /** Создаёт команду show с указанным менеджером коллекции
     * @param collectionManager менеджер коллекции
     */
    public ShowCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    /** Выполняет команду, выводя все элементы коллекции
     * @param argument аргумент команды (не используется)
     */
    @Override
    public void execute(String argument) {
        if (collectionManager.getCollection().isEmpty()) {
            System.out.println("Коллекция пуста.");
            return;
        }
        System.out.println("Элементы коллекции:");
        for (Dragon dragon : collectionManager.getCollection().values()) {
            System.out.println(dragon);
        }
    }
    /** @return описание команды */
    @Override
    public String getDescription() {
        return "вывести все элементы коллекции в строковом представлении";
    }
}