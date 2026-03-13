package model;
/**
 * Класс, представляющий координаты дракона.
 */
public class Coordinates {
    /** Координата по оси X. */
    private final int x;
    /** Координата по оси Y (не больше 91). */
    private final double y;
    /**
     * Создаёт объект координат.
     *
     * @param x координата по оси X
     * @param y координата по оси Y (должна быть ≤ 91)
     * @throws IllegalArgumentException если y больше 91
     */
    public Coordinates(int x, double y) {
        if (y > 91) throw new IllegalArgumentException("y не может быть больше 91");
        this.x = x;
        this.y = y;
    }
    /**
     * @return координата по оси X
     */
    public int getX() {
        return x;
    }
    /**
     * @return координата по оси Y
     */
    public double getY() {
        return y;
    }
    /**
     * Возвращает строковое представление координат.
     */
    @Override
    public String toString() {
        return String.format("Coordinates{x=%d, y=%.2f}", x, y);
    }
}