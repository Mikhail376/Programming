package client;

import common.model.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Менеджер ввода
 * Поддерживает интерактивный режим и выполнение скриптов.
 */
public class InputManager {
    private final Deque<Scanner> scannerStack = new ArrayDeque<>();

    private static final String INT_REGEX = "^-?\\d+$";
    private static final String DOUBLE_REGEX = "^-?\\d+(\\.\\d+)?$";

    public InputManager(Scanner scanner) {
        this.scannerStack.push(scanner);
    }

    private Scanner getCurrentScanner() {
        return scannerStack.peek();
    }

    public boolean isInteractive() {
        return scannerStack.size() == 1;
    }

    public void pushScanner(Scanner scanner) {
        scannerStack.push(scanner);
    }

    public void popScanner() {
        if (scannerStack.size() > 1) {
            Scanner current = scannerStack.pop();
            current.close();
            if (isInteractive()) {
                System.out.println("Чтение файла завершено");
            }
        }
    }

    public String readLine() {
        while (!scannerStack.isEmpty()) {
            Scanner scanner = getCurrentScanner();
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!isInteractive() && (line.isEmpty() || line.startsWith("#"))) {
                    continue;
                }
                return line;
            } else {
                popScanner();
            }
        }
        return null;
    }

    private String readValidatedString(String prompt, String regex, String errorMsg, boolean allowNull) {
        while (true) {
            if (isInteractive()) System.out.print(prompt);
            String input = readLine();
            if (input == null) return null;

            if (input.isEmpty()) {
                if (allowNull) return null;
                if (isInteractive()) System.out.println("Ошибка: поле не может быть пустым.");
                continue;
            }

            if (regex != null && !Pattern.matches(regex, input)) {
                if (isInteractive()) System.out.println(errorMsg);
                continue;
            }
            return input;
        }
    }

    public String readString(String prompt, boolean allowNull) {
        return readValidatedString(prompt, null, "", allowNull);
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            String input = readValidatedString(prompt, INT_REGEX, "Ошибка: введите целое число.", false);
            if (input == null) return 0;
            try {
                BigInteger bi = new BigInteger(input);
                if (bi.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0 ||
                        bi.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0) {
                    if (isInteractive()) System.out.println("Ошибка: число выходит за пределы int.");
                    continue;
                }
                int value = bi.intValue();
                if (value < min || value > max) {
                    if (isInteractive()) System.out.println("Ошибка: число вне диапазона [" + min + ".." + max + "]");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                if (isInteractive()) System.out.println("Ошибка: некорректное число.");
            }
        }
    }

    public Long readLong(String prompt, long min, long max, boolean allowNull) {
        while (true) {
            String input = readValidatedString(prompt, INT_REGEX, "Ошибка: введите целое число.", allowNull);
            if (input == null) return null;
            try {
                BigInteger bi = new BigInteger(input);
                if (bi.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0 ||
                        bi.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0) {
                    if (isInteractive()) System.out.println("Ошибка: число выходит за пределы long.");
                    continue;
                }
                long value = bi.longValue();
                if (value < min || value > max) {
                    if (isInteractive()) System.out.println("Ошибка: число вне диапазона.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                if (isInteractive()) System.out.println("Ошибка: некорректное число.");
            }
        }
    }

    public double readDouble(String prompt, double min) {
        while (true) {
            String input = readValidatedString(prompt, DOUBLE_REGEX, "Ошибка: введите число.", false);
            if (input == null) return 0;
            try {
                BigDecimal bd = new BigDecimal(input);
                if (bd.abs().compareTo(BigDecimal.valueOf(Double.MAX_VALUE)) > 0) {
                    if (isInteractive()) System.out.println("Ошибка: число слишком большое для double.");
                    continue;
                }
                double value = bd.doubleValue();
                if (value <= min || Double.isInfinite(value)) {
                    if (isInteractive()) System.out.println("Ошибка: число должно быть больше " + min);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                if (isInteractive()) System.out.println("Ошибка: некорректный формат числа.");
            }
        }
    }

    public float readFloat(String prompt, float min) {
        while (true) {
            String input = readValidatedString(prompt, DOUBLE_REGEX, "Ошибка: введите число.", false);
            if (input == null) return 0;
            try {
                BigDecimal bd = new BigDecimal(input);
                if (bd.abs().compareTo(BigDecimal.valueOf(Float.MAX_VALUE)) > 0) {
                    if (isInteractive()) System.out.println("Ошибка: число слишком большое для float.");
                    continue;
                }
                float value = bd.floatValue();
                if (value <= min || Float.isInfinite(value)) {
                    if (isInteractive()) System.out.println("Ошибка: число должно быть больше " + min);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                if (isInteractive()) System.out.println("Ошибка: некорректный формат.");
            }
        }
    }

    public DragonCharacter readDragonCharacter(String prompt) {
        while (true) {
            if (isInteractive()) System.out.print(prompt + " (CUNNING, WISE, EVIL, GOOD): ");
            String line = readLine();
            if (line == null) return null;
            line = line.trim().toUpperCase();
            try {
                return DragonCharacter.valueOf(line);
            } catch (IllegalArgumentException e) {
                if (isInteractive()) System.out.println("Ошибка: выберите характер из списка.");
            }
        }
    }

    public Coordinates readCoordinates() {
        if (isInteractive()) System.out.println("Ввод координат:");
        int x = readInt("  Координата X: ", Integer.MIN_VALUE, Integer.MAX_VALUE);
        double y = readDouble("  Координата Y (<= 91): ", -Double.MAX_VALUE);
        while (y > 91) {
            if (isInteractive()) System.out.println("Ошибка: Y не может быть больше 91.");
            y = readDouble("  Координата Y (<= 91): ", -Double.MAX_VALUE);
        }
        return new Coordinates(x, y);
    }

    public DragonHead readDragonHead() {
        if (isInteractive()) System.out.print("Создать голову? (y/n или Enter для null): ");
        String answer = readLine();
        if (answer == null || !answer.trim().equalsIgnoreCase("y")) return null;

        Long size = readLong("  Размер головы: ", 1, Long.MAX_VALUE, false);
        long eyesCount = readLong("  Количество глаз: ", 1, Long.MAX_VALUE, false);
        double toothCount = readDouble("  Количество зубов: ", 0);
        return new DragonHead(size, eyesCount, toothCount);
    }

    public Dragon readDragon() {
        if (isInteractive()) System.out.println("Ввод данных дракона");
        String name = readString("Имя: ", false);
        if (name == null) return null;

        Coordinates coordinates = readCoordinates();
        Long age = readLong("Возраст (или Enter для null): ", 1, Long.MAX_VALUE, true);
        double wingspan = readDouble("Размах крыльев: ", 0);
        float weight = readFloat("Вес: ", 0);
        DragonCharacter character = readDragonCharacter("Характер");
        DragonHead head = readDragonHead();

        return new Dragon(0L, name, coordinates, LocalDateTime.now(), age, wingspan, weight, character, head);
    }
}