package common.model;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * Модель дракона.
 * Поля id и creationDate генерируются сервером.
 */
public class Dragon implements Comparable<Dragon>, Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private Long age;
    private double wingspan;
    private float weight;
    private DragonCharacter character;
    private DragonHead head;

    public Dragon(long id, String name, Coordinates coordinates, LocalDateTime creationDate,
                  Long age, double wingspan, float weight, DragonCharacter character, DragonHead head) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Имя не может быть пустым");
        if (coordinates == null) throw new IllegalArgumentException("Координаты не могут быть null");
        if (age != null && age <= 0) throw new IllegalArgumentException("Возраст должен быть > 0");
        if (wingspan <= 0) throw new IllegalArgumentException("Размах крыльев должен быть > 0");
        if (weight <= 0) throw new IllegalArgumentException("Вес должен быть > 0");
        if (character == null) throw new IllegalArgumentException("Характер не может быть null");

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

    public long getId() { return id; }
    public String getName() { return name; }
    public Coordinates getCoordinates() { return coordinates; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public Long getAge() { return age; }
    public double getWingspan() { return wingspan; }
    public float getWeight() { return weight; }
    public DragonCharacter getCharacter() { return character; }
    public DragonHead getHead() { return head; }

    public void setId(long id) {
        if (id <= 0) throw new IllegalArgumentException("ID должен быть > 0");
        this.id = id;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        if (creationDate == null) throw new IllegalArgumentException("Дата не может быть null");
        this.creationDate = creationDate;
    }

    @Override
    public int compareTo(Dragon other) {
        return Long.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return String.format(
                "Dragon{id=%d, name='%s', %s, age=%s, wingspan=%.2f, weight=%.2f, character=%s, head=%s, created=%s}",
                id, name, coordinates, age, wingspan, weight, character, head,
                (creationDate != null ? creationDate.toString() : "null"));
    }
}