package manager;
import model.Dragon;
import java.time.LocalDateTime;
import java.util.TreeMap;
/**
 * Класс для управления коллекцией драконов.
 */
public class CollectionManager {
    /** Коллекция драконов, хранящаяся в виде TreeMap. */
    private final TreeMap<Long, Dragon> collection = new TreeMap<>();
    /** Дата создания коллекции. */
    private final LocalDateTime creationDate = LocalDateTime.now();
    /**
     * @return коллекция драконов
     */
    public TreeMap<Long, Dragon> getCollection() {
        return collection;
    }
    /**
     * @return дата создания коллекции
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    /**
     * Генерирует следующий уникальный id для дракона.
     *
     * @return следующий id
     */
    public long generateNextId() {
        if (collection.isEmpty()) return 1L;
        return collection.values().stream()
                .mapToLong(Dragon::getId)
                .max()
                .getAsLong() + 1;
    }
    /**
     * Очищает коллекцию.
     */
    public void clear() {
        collection.clear();
    }
    /**
     * Обновляет элемент коллекции по ключу.
     *
     * @param key ключ элемента
     * @param newDragon новый объект дракона
     */
    public void update(long key, Dragon newDragon) {
        collection.put(key, newDragon);
    }
}