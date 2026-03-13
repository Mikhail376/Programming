package commands;
import manager.CollectionManager;
import manager.InputManager;
import model.Dragon;
/** Команда replace_if_greater: заменяет значение по ключу, если новое больше старого */
public class ReplaceIfGreaterCommand implements Command {
    /** Менеджер коллекции, используемый для получения и замены элемента */
    private final CollectionManager collectionManager;
    /** Менеджер ввода, используемый для создания нового дракона */
    private final InputManager inputManager;
    /** Создаёт команду replace_if_greater с указанными менеджерами
     * @param collectionManager менеджер коллекции
     * @param inputManager менеджер ввода
     */
    public ReplaceIfGreaterCommand(CollectionManager collectionManager, InputManager inputManager) {
        this.collectionManager = collectionManager;
        this.inputManager = inputManager;
    }
    /** Выполняет команду, заменяя элемент по ключу, если новое значение больше старого
     * @param argument ключ элемента
     */
    @Override
    public void execute(String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            System.out.println("Ошибка: Укажите ключ. Формат: replace_if_greater {key}");
            return;
        }
        try {
            Long key = Long.parseLong(argument);
            Dragon oldDragon = collectionManager.getCollection().get(key);
            if (oldDragon == null) {
                System.out.println("Элемент не найден.");
                return;
            }
            System.out.println("Введите данные нового дракона для сравнения:");
            Dragon newDragon = inputManager.readDragon(oldDragon.getId());
            if (newDragon.compareTo(oldDragon) > 0) {
                collectionManager.getCollection().put(key, newDragon);
                System.out.println("Элемент успешно заменен.");
            } else {
                System.out.println("Новое значение не больше старого. Замена отменена.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Ключ должен быть целым числом.");
        }
    }
    /** @return описание команды */
    @Override
    public String getDescription() {
        return "заменить значение по ключу, если новое больше старого";
    }
}