package commands;
import manager.CollectionManager;
import manager.InputManager;
import model.Dragon;
/** Команда update: обновляет элемент коллекции по указанному ключу */
public class UpdateCommand implements Command {
    /** Менеджер коллекции, используемый для обновления элемента */
    private final CollectionManager collectionManager;
    /** Менеджер ввода, используемый для создания нового дракона */
    private final InputManager inputManager;
    /** Создаёт команду update с указанными менеджерами
     * @param collectionManager менеджер коллекции
     * @param inputManager менеджер ввода
     */
    public UpdateCommand(CollectionManager collectionManager, InputManager inputManager) {
        this.collectionManager = collectionManager;
        this.inputManager = inputManager;
    }
    /** Выполняет команду, обновляя элемент коллекции по ключу
     * @param argument ключ элемента
     */
    @Override
    public void execute(String argument) {
        if (argument == null) {
            System.out.println("Не указан ключ для обновления.");
            return;
        }
        try {
            long key = Long.parseLong(argument);
            if (!collectionManager.getCollection().containsKey(key)) {
                System.out.println("Элемент с ключом " + key + " не найден.");
                return;
            }
            Dragon updatedDragon = inputManager.readDragon(key);
            collectionManager.update(key, updatedDragon);
            System.out.println("Элемент с ключом " + key + " обновлён.");
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ключа: " + argument);
        }
    }
    /** @return описание команды */
    @Override
    public String getDescription() {
        return "обновить значение элемента коллекции по ключу";
    }
}