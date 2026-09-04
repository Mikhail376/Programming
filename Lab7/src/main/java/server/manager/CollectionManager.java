package server.manager;

import common.model.Dragon;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class CollectionManager {
    private final ReentrantLock lock = new ReentrantLock();
    private final NavigableMap<Long, Dragon> collection = new TreeMap<>();
    private final LocalDateTime creationDate = LocalDateTime.now();
    private final DatabaseManager dbManager;

    public CollectionManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }


    public void loadFromDB() throws Exception {
        lock.lock();
        try { collection.putAll(dbManager.loadAll()); }
        finally { lock.unlock(); }
    }

    public LocalDateTime getCreationDate() { return creationDate; }
    public int size() { lock.lock(); try { return collection.size(); } finally { lock.unlock(); } }
    public boolean containsKey(Long key) { lock.lock(); try { return collection.containsKey(key); } finally { lock.unlock(); } }
    public Dragon get(Long key) { lock.lock(); try { return collection.get(key); } finally { lock.unlock(); } }
    public Collection<Dragon> getAll() { lock.lock(); try { return Collections.unmodifiableCollection(new ArrayList<>(collection.values())); } finally { lock.unlock(); } }
    public NavigableMap<Long, Dragon> getCollectionMap() { lock.lock(); try { return Collections.unmodifiableNavigableMap(new TreeMap<>(collection)); } finally { lock.unlock(); } }


    public boolean insert(Long key, Dragon dragon) throws Exception {
        long generatedId = dbManager.insertDragon(dragon, key);
        if (generatedId > 0) {
            lock.lock();
            try {
                dragon.setId(generatedId);
                collection.put(key, dragon);
                return true;
            } finally { lock.unlock(); }
        }
        return false;
    }

    public boolean update(Long key, Dragon dragon) throws Exception {
        lock.lock();
        Dragon existing = collection.get(key);
        if (existing == null) { lock.unlock(); return false; }
        dragon.setId(existing.getId());
        dragon.setCreationDate(existing.getCreationDate());
        lock.unlock();

        if (dbManager.updateDragon(dragon, key)) {
            lock.lock();
            try { collection.put(key, dragon); return true; }
            finally { lock.unlock(); }
        }
        return false;
    }

    public boolean remove(Long key) throws Exception {
        if (dbManager.removeDragon(key)) {
            lock.lock();
            try { return collection.remove(key) != null; }
            finally { lock.unlock(); }
        }
        return false;
    }

    public void clearByOwner(String owner) throws Exception {
        lock.lock();
        List<Long> keysToRemove;
        try {
            keysToRemove = collection.entrySet().stream()
                    .filter(e -> e.getValue().getOwner().equals(owner))
                    .map(Map.Entry::getKey)
                    .toList();
        } finally { lock.unlock(); }

        for (Long key : keysToRemove) remove(key);
    }

    public int removeGreater(Dragon ref, String owner) throws Exception {
        lock.lock();
        List<Long> toRemove;
        try {
            toRemove = collection.entrySet().stream()
                    .filter(e -> e.getValue().getOwner().equals(owner))
                    .filter(e -> e.getValue().compareTo(ref) > 0)
                    .map(Map.Entry::getKey)
                    .toList();
        } finally { lock.unlock(); }

        int count = 0;
        for (Long key : toRemove) if (remove(key)) count++;
        return count;
    }

    public long countByWeight(float weight) {
        lock.lock();
        try {
            return collection.values().stream()
                    .filter(d -> Float.compare(d.getWeight(), weight) == 0)
                    .count();
        } finally { lock.unlock(); }
    }

    public List<Dragon> getSortedList() {
        lock.lock();
        try {
            return collection.values().stream().sorted().collect(Collectors.toList());
        } finally { lock.unlock(); }
    }
}