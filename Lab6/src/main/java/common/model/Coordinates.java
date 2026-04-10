package common.model;
import java.io.Serializable;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int x;
    private final double y;
    public Coordinates(int x, double y) {
        if (y > 91) throw new IllegalArgumentException("y не может быть больше 91");
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    @Override
    public String toString() {
        return String.format("Coordinates{x=%d, y=%.2f}", x, y);
    }
}