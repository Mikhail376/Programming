package commands;
import manager.CollectionManager;
import manager.InputManager;
import model.Dragon;
/** Команда insert: добавляет новый элемент в коллекцию с указанным ключом */
public class InsertCommand implements Command {
    /** Менеджер коллекции, используемый для добавления элемента */
    private final CollectionManager collectionManager;
    /** Менеджер ввода, используемый для создания нового дракона */
    private final InputManager inputManager;
    /** Создаёт команду insert с указанными менеджерами
     * @param collectionManager менеджер коллекции
     * @param inputManager менеджер ввода
     */
    public InsertCommand(CollectionManager collectionManager, InputManager inputManager) {
        this.collectionManager = collectionManager;
        this.inputManager = inputManager;
    }
    /** Выполняет команду, добавляя новый элемент с заданным ключом
     * @param argument ключ для нового элемента
     */
    @Override
    public void execute(String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            System.out.println("Ошибка: Укажите ключ. Формат: insert {key}");
            return;
        }
        try {
            Long key = Long.parseLong(argument);
            if (collectionManager.getCollection().containsKey(key)) {
                System.out.println("Ошибка: Элемент с таким ключом уже существует.");
                return;
            }
            long newId = collectionManager.generateNextId();
            System.out.println("Введите данные для дракона (ID: " + newId + "):");
            Dragon dragon = inputManager.readDragon(newId);
            collectionManager.getCollection().put(key, dragon);
            System.out.println("Элемент успешно добавлен.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Ключ должен быть целым числом.");
        }
    }
    /** @return описание команды */
    @Override
    public String getDescription() {
        return "добавить новый элемент с заданным ключом";
    }
}