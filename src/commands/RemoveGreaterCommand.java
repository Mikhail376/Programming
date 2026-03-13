package commands;
import manager.CollectionManager;
import manager.InputManager;
import model.Dragon;
import java.util.Iterator;
import java.util.Map;
/** Команда remove_greater: удаляет из коллекции все элементы, превышающие заданный */
public class RemoveGreaterCommand implements Command {
    /** Менеджер коллекции, используемый для удаления элементов */
    private final CollectionManager collectionManager;
    /** Менеджер ввода, используемый для создания эталонного дракона */
    private final InputManager inputManager;
    /** Создаёт команду remove_greater с указанными менеджерами
     * @param collectionManager менеджер коллекции
     * @param inputManager менеджер ввода
     */
    public RemoveGreaterCommand(CollectionManager collectionManager, InputManager inputManager) {
        this.collectionManager = collectionManager;
        this.inputManager = inputManager;
    }
    /** Выполняет команду, удаляя все элементы, превышающие заданный
     * @param argument аргумент команды (не используется)
     */
    @Override
    public void execute(String argument) {
        long tempId = -1; // временный id для объекта
        Dragon referenceDragon = inputManager.readDragon(tempId);
        Iterator<Map.Entry<Long, Dragon>> iterator = collectionManager.getCollection().entrySet().iterator();
        int removedCount = 0;
        while (iterator.hasNext()) {
            Map.Entry<Long, Dragon> entry = iterator.next();
            if (entry.getValue().compareTo(referenceDragon) > 0) {
                iterator.remove();
                removedCount++;
            }
        }
        System.out.println("Удалено элементов, превышающих заданный: " + removedCount);
    }
    /** @return описание команды */
    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, превышающие заданный";
    }
}