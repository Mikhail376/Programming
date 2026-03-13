package manager;
import model.*;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.regex.Pattern;
/**
 * Класс для управления пользовательским вводом и валидацией данных.
 */
public class InputManager {
    /** Сканер для чтения пользовательского ввода. */
    private final Scanner scanner;
    /** Регулярное выражение для целых чисел. */
    private static final String INT_REGEX = "^-?\\d+$";
    /** Регулярное выражение для чисел с плавающей точкой. */
    private static final String DOUBLE_REGEX = "^-?\\d+(\\.\\d+)?$";
    /**
     * Создаёт InputManager с указанным сканером.
     *
     * @param scanner сканер для чтения ввода
     */
    public InputManager(Scanner scanner) {
        this.scanner = scanner;
    }
    /**
     * Читает строку из ввода.
     *
     * @return введённая строка или null
     */
    public String readLine() {
        if (!scanner.hasNextLine()) return null;
        return scanner.nextLine();
    }
    /**
     * Читает строку с проверкой по регулярному выражению и дополнительными условиями.
     */
    private String readValidatedString(String prompt, String regex, String errorMsg, boolean allowNull) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                if (allowNull) return null;
                System.out.println("Ошибка: поле не может быть пустым.");
                continue;
            }
            if (input.length() > 40) {
                System.out.println("Ошибка: введено слишком длинное значение.");
                continue;
            }
            if (regex != null && !Pattern.matches(regex, input)) {
                System.out.println(errorMsg);
                continue;
            }
            return input;
        }
    }
    /**
     * Читает строку с возможностью пустого ввода.
     *
     * @param prompt текст подсказки
     * @param allowEmpty разрешён пустой ввод
     * @return введённая строка
     */
    public String readString(String prompt, boolean allowEmpty) {
        return readValidatedString(prompt, null, "", allowEmpty);
    }
    /**
     * Читает целое число в заданном диапазоне.
     */
    public int readInt(String prompt, int min, int max) {
        while (true) {
            String input = readValidatedString(prompt, INT_REGEX, "Ошибка: введите целое число.", false);
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("Ошибка: число вне диапазона [" + min + ".." + max + "]");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: число слишком велико.");
            }
        }
    }
    /**
     * Читает длинное число (Long) с проверкой диапазона и возможности null.
     */
    public Long readLong(String prompt, long min, long max, boolean allowNull) {
        while (true) {
            String input = readValidatedString(prompt, INT_REGEX, "Ошибка: введите целое число.", allowNull);
            if (input == null) return null;
            try {
                long value = Long.parseLong(input);
                if (value < min || value > max) {
                    System.out.println("Ошибка: число вне диапазона.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: число слишком велико.");
            }
        }
    }
    /**
     * Читает число с плавающей точкой (double) больше минимального значения.
     */
    public double readDouble(String prompt, double min) {
        while (true) {
            String input = readValidatedString(prompt, DOUBLE_REGEX, "Ошибка: введите число (например, 10.5).", false);
            try {
                double value = Double.parseDouble(input);
                if (value <= min || Double.isInfinite(value)) {
                    System.out.println("Ошибка: некорректное число или оно меньше/равно " + min);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: некорректный формат числа.");
            }
        }
    }
    /**
     * Читает число с плавающей точкой (float) больше минимального значения.
     */
    public float readFloat(String prompt, float min) {
        while (true) {
            String input = readValidatedString(prompt, DOUBLE_REGEX, "Ошибка: введите число.", false);
            try {
                float value = Float.parseFloat(input);
                if (value <= min || Float.isInfinite(value)) {
                    System.out.println("Ошибка: некорректное число или оно меньше/равно " + min);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: некорректный формат.");
            }
        }
    }
    /**
     * Читает характер дракона из списка.
     *
     * @return выбранный DragonCharacter
     */
    public DragonCharacter readDragonCharacter(String prompt) {
        while (true) {
            System.out.print(prompt + " (CUNNING, WISE, EVIL, GOOD): ");
            String line = scanner.nextLine().trim().toUpperCase();
            try {
                return DragonCharacter.valueOf(line);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: выберите характер из списка.");
            }
        }
    }
    /**
     * Читает координаты дракона.
     *
     * @return объект Coordinates
     */
    public Coordinates readCoordinates() {
        int x = readInt("Координата X: ", Integer.MIN_VALUE, Integer.MAX_VALUE);
        double y = readDouble("Координата Y (<=91): ", -Double.MAX_VALUE);
        while (y > 91) {
            System.out.println("Ошибка: Y должен быть <= 91.");
            y = readDouble("Координата Y (<=91): ", -Double.MAX_VALUE);
        }
        return new Coordinates(x, y);
    }
    /**
     * Читает данные головы дракона.
     *
     * @return объект DragonHead или null
     */
    public DragonHead readDragonHead() {
        System.out.println("Ввод головы (нажмите Enter для null):");
        Long size = readLong("Размер головы: ", 1, Long.MAX_VALUE, true);
        if (size == null) return null;
        long eyes = readLong("Количество глаз: ", 1, Long.MAX_VALUE, false);
        double teeth = readDouble("Количество зубов: ", -Double.MAX_VALUE);
        return new DragonHead(size, eyes, teeth);
    }
    /**
     * Читает все данные для создания дракона.
     *
     * @param id уникальный идентификатор дракона
     * @return объект Dragon
     */
    public Dragon readDragon(long id) {
        String name = readString("Имя: ", false);
        Coordinates coordinates = readCoordinates();
        Long age = readLong("Возраст (или Enter): ", 1, Long.MAX_VALUE, true);
        double wingspan = readDouble("Размах крыльев: ", 0);
        float weight = readFloat("Вес: ", 0);
        DragonCharacter character = readDragonCharacter("Характер");
        DragonHead head = readDragonHead();
        return new Dragon(id, name, coordinates, LocalDateTime.now(), age, wingspan, weight, character, head);
    }
}