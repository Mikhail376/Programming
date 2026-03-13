package commands;
import manager.CollectionManager;
/** Команда remove_key: удаляет элемент из коллекции по указанному ключу */
public class RemoveKeyCommand implements Command {
    /** Менеджер коллекции, используемый для удаления элемента */
    private final CollectionManager collectionManager;
    /** Создаёт команду remove_key с указанным менеджером коллекции
     * @param collectionManager менеджер коллекции
     */
    public RemoveKeyCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    /** Выполняет команду, удаляя элемент по ключу
     * @param argument ключ элемента
     */
    @Override
    public void execute(String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            System.out.println("Ошибка: Укажите ключ. Формат: remove_key {key}");
            return;
        }
        try {
            Long key = Long.parseLong(argument);
            if (collectionManager.getCollection().containsKey(key)) {
                collectionManager.getCollection().remove(key);
                System.out.println("Элемент удалён.");
            } else {
                System.out.println("Элемент с ключом " + key + " не найден.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Ключ должен быть целым числом.");
        }
    }
    /** @return описание команды */
    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по ключу";
    }
}