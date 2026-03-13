package commands;
import manager.CollectionManager;
/** Команда info: выводит информацию о коллекции */
public class InfoCommand implements Command {
    /** Менеджер коллекции, используемый для получения информации */
    private final CollectionManager collectionManager;
    /** Создаёт команду info с указанным менеджером коллекции
     * @param collectionManager менеджер коллекции
     */
    public InfoCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    /** Выполняет команду, выводя тип коллекции, дату инициализации и количество элементов
     * @param argument аргумент команды (не используется)
     */
    @Override
    public void execute(String argument) {
        System.out.println("Информация о коллекции:");
        System.out.println("Тип коллекции: " + collectionManager.getCollection().getClass().getName());
        System.out.println("Дата инициализации: " + collectionManager.getCreationDate());
        System.out.println("Количество элементов: " + collectionManager.getCollection().size());
    }
    /** @return описание команды */
    @Override
    public String getDescription() {
        return "вывести информацию о коллекции (тип, дата инициализации, количество элементов)";
    }
}