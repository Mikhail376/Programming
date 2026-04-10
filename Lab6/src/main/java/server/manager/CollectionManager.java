package server.manager;

import common.model.Dragon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Менеджер коллекции. Отвечает за хранение и низкоуровневую обработку данных.
 * Все операции реализованы через Stream API.
 */
public class CollectionManager {
    private static final Logger logger = LoggerFactory.getLogger(CollectionManager.class);

    private final NavigableMap<Long, Dragon> collection = Collections.synchronizedNavigableMap(new TreeMap<>());
    private final LocalDateTime creationDate = LocalDateTime.now();

    public NavigableMap<Long, Dragon> getCollection() {
        return collection;
    }
    /**
     * Загружает коллекцию из файла.
     */
    public void loadCollection(FileManager fileManager) {
        Map<Long, Dragon> loaded = fileManager.load();
        if (loaded != null) {
            collection.putAll(loaded);
            logger.info("Загружено элементов: {}", loaded.size());
        }
    }

    /**
     * Генерирует уникальный ID, используя Stream API.
     */
    public long generateNextId() {
        return collection.values().stream()
                .mapToLong(Dragon::getId)
                .max()
                .orElse(0L) + 1;
    }

    /**
     * Проверяет существование ID через Stream API.
     */
    public boolean isIdExists(long id) {
        return collection.values().stream()
                .anyMatch(d -> d.getId() == id);
    }

    /**
     * Возвращает краткую информацию о коллекции.
     */
    public String getInfo() {
        return "Тип: TreeMap\n" +
                "Дата инициализации: " + creationDate + "\n" +
                "Количество элементов: " + collection.size();
    }

    /**
     * Очистка коллекции.
     */
    public void clear() {
        collection.clear();
    }

    /**
     * Удаление элементов, которые больше заданного.
     */
    public int removeGreater(Dragon dragon) {
        int initialSize = collection.size();
        List<Long> keysToRemove = collection.entrySet().stream()
                .filter(entry -> entry.getValue().compareTo(dragon) > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        keysToRemove.forEach(collection::remove);
        return initialSize - collection.size();
    }

    /**
     * Поиск по значению веса.
     */
    public long countByWeight(float weight) {
        return collection.values().stream()
                .filter(d -> Float.compare(d.getWeight(), weight) == 0)
                .count();
    }

    /**
     * Получение отсортированного списка
     */
    public List<Dragon> getSortedList() {
        return collection.values().stream()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Удаление по ключу.
     */
    public boolean remove(long key) {
        return collection.remove(key) != null;
    }

    /**
     * Добавление/обновление.
     */
    public void insert(long key, Dragon dragon) {
        collection.put(key, dragon);
    }
    /**
     * Возвращает дату создания коллекции (используется в InfoCommand).
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}