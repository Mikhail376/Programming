package commands;
import manager.CollectionManager;
import manager.FileManager;
/** Команда save: сохраняет коллекцию в файл */
public class SaveCommand implements Command {
    /** Менеджер коллекции, содержащей элементы для сохранения */
    private final CollectionManager collectionManager;
    /** Менеджер файлов, используемый для сохранения коллекции */
    private final FileManager fileManager;
    /** Создаёт команду save с указанными менеджерами
     * @param collectionManager менеджер коллекции
     * @param fileManager менеджер файлов
     */
    public SaveCommand(CollectionManager collectionManager, FileManager fileManager) {
        this.collectionManager = collectionManager;
        this.fileManager = fileManager;
    }
    /** Выполняет команду, сохраняя коллекцию в файл
     * @param argument аргумент команды (не используется)
     */
    @Override
    public void execute(String argument) {
        try {
            fileManager.save(collectionManager.getCollection());
            System.out.println("Коллекция успешно сохранена в файл.");
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении коллекции: " + e.getMessage());
        }
    }
    /** @return описание команды */
    @Override
    public String getDescription() {
        return "сохранить коллекцию в файл";
    }
}