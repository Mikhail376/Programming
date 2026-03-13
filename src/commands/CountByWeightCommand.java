package commands;
import manager.CollectionManager;
/** Команда count_by_weight: выводит количество элементов, значение поля weight которых равно заданному */
public class CountByWeightCommand implements Command {
    /** Менеджер коллекции, с которым работает команда */
    private final CollectionManager collectionManager;
    /** Создаёт команду count_by_weight с указанным менеджером коллекции
     * @param collectionManager менеджер коллекции
     */
    public CountByWeightCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    /** Выполняет команду, подсчитывая элементы с указанным весом
     * @param argument строковое значение веса
     */
    @Override
    public void execute(String argument) {
        if (argument == null) {
            System.out.println("Не указано значение веса.");
            return;
        }
        try {
            float weight = Float.parseFloat(argument);
            long count = collectionManager.getCollection().values().stream()
                    .filter(d -> d.getWeight() == weight)
                    .count();
            System.out.println("Количество элементов с весом " + weight + ": " + count);
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат веса: " + argument);
        }
    }
    /** @return описание команды */
    @Override
    public String getDescription() {
        return "вывести количество элементов с заданным весом";
    }
}