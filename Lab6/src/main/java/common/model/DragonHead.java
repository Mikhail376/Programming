package common.model;
import java.io.Serializable;

public class DragonHead implements Serializable {
    private static final long serialVersionUID = 1L;
    private final long size;
    private final long eyesCount;
    private final Double toothCount;
    public DragonHead(long size, long eyesCount, Double toothCount) {
        if (toothCount == null) throw new IllegalArgumentException("toothCount не может быть null");
        this.size = size;
        this.eyesCount = eyesCount;
        this.toothCount = toothCount;
    }
    public long getSize() {
        return size;
    }
    public long getEyesCount() {
        return eyesCount;
    }
    public Double getToothCount() {
        return toothCount;
    }

    @Override
    public String toString() {
        return String.format("DragonHead{size=%d, eyesCount=%d, toothCount=%.2f}", size, eyesCount, toothCount);
    }
}