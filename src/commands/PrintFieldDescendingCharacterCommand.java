package commands;
import manager.CollectionManager;
import model.Dragon;
import model.DragonCharacter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
/** Команда print_field_descending_character: выводит значения поля character всех элементов в порядке убывания */
public class PrintFieldDescendingCharacterCommand implements Command {
    /** Менеджер коллекции, используемый для получения элементов */
    private final CollectionManager collectionManager;
    /** Создаёт команду print_field_descending_character с указанным менеджером коллекции
     * @param collectionManager менеджер коллекции
     */
    public PrintFieldDescendingCharacterCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    /** Выполняет команду, выводя значения поля character всех элементов в порядке убывания
     * @param argument аргумент команды (не используется)
     */
    @Override
    public void execute(String argument) {
        List<DragonCharacter> characters = collectionManager.getCollection().values().stream()
                .map(Dragon::getCharacter)
                .filter(c -> c != null)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        if (characters.isEmpty()) {
            System.out.println("Коллекция пуста или у элементов нет поля character.");
        } else {
            System.out.println("Значения поля character в порядке убывания:");
            characters.forEach(System.out::println);
        }
    }
    /** @return описание команды */
    @Override
    public String getDescription() {
        return "вывести значения поля character всех элементов в порядке убывания";
    }
}