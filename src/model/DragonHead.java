package model;
/**
 * Класс, описывающий голову дракона.
 */
public class DragonHead {
    /** Размер головы. */
    private final long size;
    /** Количество глаз. */
    private final long eyesCount;
    /** Количество зубов (не может быть null). */
    private final Double toothCount;
    /**
     * Создаёт объект головы дракона.
     *
     * @param size размер головы
     * @param eyesCount количество глаз
     * @param toothCount количество зубов
     * @throws IllegalArgumentException если toothCount равен null
     */
    public DragonHead(long size, long eyesCount, Double toothCount) {
        if (toothCount == null) throw new IllegalArgumentException("toothCount не может быть null");
        this.size = size;
        this.eyesCount = eyesCount;
        this.toothCount = toothCount;
    }
    /** @return размер головы */
    public long getSize() {
        return size;
    }
    /** @return количество глаз */
    public long getEyesCount() {
        return eyesCount;
    }
    /** @return количество зубов */
    public Double getToothCount() {
        return toothCount;
    }
    /**
     * Возвращает строковое представление головы дракона.
     */
    @Override
    public String toString() {
        return String.format("DragonHead{size=%d, eyesCount=%d, toothCount=%.2f}", size, eyesCount, toothCount);
    }
}