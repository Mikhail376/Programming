package model;
import java.time.LocalDateTime;
/**
 * Класс, описывающий дракона и его характеристики.
 */
public class Dragon implements Comparable<Dragon> {
    /** Уникальный идентификатор дракона (>0). */
    private final long id;
    /** Имя дракона (не может быть null или пустым). */
    private final String name;
    /** Координаты дракона (не null). */
    private final Coordinates coordinates;
    /** Дата создания объекта. */
    private final LocalDateTime creationDate;
    /** Возраст дракона (>0, может быть null). */
    private final Long age;
    /** Размах крыльев (>0). */
    private final double wingspan;
    /** Вес дракона (>0). */
    private final float weight;
    /** Характер дракона. */
    private final DragonCharacter character;
    /** Голова дракона (может быть null). */
    private final DragonHead head;
    /**
     * Создаёт объект дракона.
     *
     * @param id уникальный идентификатор (>0)
     * @param name имя дракона
     * @param coordinates координаты дракона
     * @param creationDate дата создания
     * @param age возраст дракона (>0, может быть null)
     * @param wingspan размах крыльев (>0)
     * @param weight вес дракона (>0)
     * @param character характер дракона
     * @param head голова дракона
     * @throws IllegalArgumentException если параметры имеют недопустимые значения
     */
    public Dragon(long id, String name, Coordinates coordinates, LocalDateTime creationDate,
                  Long age, double wingspan, float weight, DragonCharacter character, DragonHead head) {
        if (id <= 0) throw new IllegalArgumentException("id должно быть > 0");
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("name не может быть пустым");
        if (coordinates == null) throw new IllegalArgumentException("coordinates не может быть null");
        if (creationDate == null) throw new IllegalArgumentException("creationDate не может быть null");
        if (age != null && age <= 0) throw new IllegalArgumentException("age должно быть > 0");
        if (wingspan <= 0) throw new IllegalArgumentException("wingspan должно быть > 0");
        if (weight <= 0) throw new IllegalArgumentException("weight должно быть > 0");
        if (character == null) throw new IllegalArgumentException("character не может быть null");
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.age = age;
        this.wingspan = wingspan;
        this.weight = weight;
        this.character = character;
        this.head = head;
    }
    /** @return id дракона */
    public long getId() { return id; }
    /** @return имя дракона */
    public String getName() { return name; }
    /** @return координаты дракона */
    public Coordinates getCoordinates() { return coordinates; }
    /** @return дата создания */
    public LocalDateTime getCreationDate() { return creationDate; }
    /** @return возраст дракона */
    public Long getAge() { return age; }
    /** @return размах крыльев */
    public double getWingspan() { return wingspan; }
    /** @return вес дракона */
    public float getWeight() { return weight; }
    /** @return характер дракона */
    public DragonCharacter getCharacter() { return character; }
    /** @return голова дракона */
    public DragonHead getHead() { return head; }
    /**
     * Сравнивает драконов по их id.
     *
     * @param other другой дракон
     * @return результат сравнения
     */
    @Override
    public int compareTo(Dragon other) {
        return Long.compare(this.id, other.id);
    }
    /**
     * Возвращает строковое представление дракона.
     */
    @Override
    public String toString() {
        return String.format(
                "Dragon{id=%d, name='%s', coordinates=%s, age=%s, wingspan=%.2f, weight=%.2f, character=%s, head=%s}",
                id, name, coordinates, age, wingspan, weight, character, head);
    }
}