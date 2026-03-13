package manager;
import model.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.TreeMap;
/**
 * Класс для работы с файлом XML, содержащим коллекцию драконов.
 * <p>
 * Позволяет загружать коллекцию из файла и сохранять её обратно.
 */
public class FileManager {
    /** Имя файла для чтения и записи коллекции. */
    private final String fileName;
    /**
     * Создаёт объект FileManager для заданного файла.
     *
     * @param fileName имя XML-файла
     */
    public FileManager(String fileName) {
        this.fileName = fileName;
    }
    /**
     * Загружает коллекцию драконов из XML-файла.
     *
     * @return TreeMap с драконами, ключ — id дракона
     */
    public TreeMap<Long, Dragon> load() {
        TreeMap<Long, Dragon> collection = new TreeMap<>();
        File file = new File(fileName);
        if (!file.exists() || file.length() == 0) return collection;
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(bis);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("dragon");
            for (int i = 0; i < nodeList.getLength(); i++) {
                try {
                    Element el = (Element) nodeList.item(i);
                    long id = Long.parseLong(el.getElementsByTagName("id").item(0).getTextContent());
                    String name = el.getElementsByTagName("name").item(0).getTextContent();
                    int x = Integer.parseInt(el.getElementsByTagName("x").item(0).getTextContent());
                    double y = Double.parseDouble(el.getElementsByTagName("y").item(0).getTextContent());
                    LocalDateTime date = LocalDateTime.parse(el.getElementsByTagName("creationDate").item(0).getTextContent());
                    String ageStr = el.getElementsByTagName("age").item(0).getTextContent();
                    Long age = ageStr.equals("null") ? null : Long.parseLong(ageStr);
                    double wingspan = Double.parseDouble(el.getElementsByTagName("wingspan").item(0).getTextContent());
                    float weight = Float.parseFloat(el.getElementsByTagName("weight").item(0).getTextContent());
                    DragonCharacter character = DragonCharacter.valueOf(el.getElementsByTagName("character").item(0).getTextContent());
                    DragonHead head = null;
                    NodeList headNodes = el.getElementsByTagName("head");
                    if (headNodes.getLength() > 0 && !headNodes.item(0).getTextContent().equals("null")) {
                        Element hEl = (Element) headNodes.item(0);
                        head = new DragonHead(
                                Long.parseLong(hEl.getElementsByTagName("size").item(0).getTextContent()),
                                Long.parseLong(hEl.getElementsByTagName("eyesCount").item(0).getTextContent()),
                                Double.parseDouble(hEl.getElementsByTagName("toothCount").item(0).getTextContent())
                        );
                    }
                    Dragon dragon = new Dragon(id, name, new Coordinates(x, y), date, age, wingspan, weight, character, head);
                    collection.put(id, dragon);
                } catch (Exception e) {
                    System.out.println("Предупреждение: Пропущен некорректный элемент в XML.");
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при чтении XML: " + e.getMessage());
        }
        return collection;
    }
    /**
     * Сохраняет коллекцию драконов в XML-файл.
     *
     * @param collection коллекция драконов для сохранения
     */
    public void save(TreeMap<Long, Dragon> collection) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<dragons>");
            for (Dragon d : collection.values()) {
                writer.println("  <dragon>");
                writer.println("    <id>" + d.getId() + "</id>");
                writer.println("    <name>" + d.getName() + "</name>");
                writer.println("    <coordinates><x>" + d.getCoordinates().getX() + "</x><y>" + d.getCoordinates().getY() + "</y></coordinates>");
                writer.println("    <creationDate>" + d.getCreationDate() + "</creationDate>");
                writer.println("    <age>" + (d.getAge() == null ? "null" : d.getAge()) + "</age>");
                writer.println("    <wingspan>" + d.getWingspan() + "</wingspan>");
                writer.println("    <weight>" + d.getWeight() + "</weight>");
                writer.println("    <character>" + d.getCharacter() + "</character>");
                if (d.getHead() != null) {
                    writer.println("    <head>");
                    writer.println("      <size>" + d.getHead().getSize() + "</size>");
                    writer.println("      <eyesCount>" + d.getHead().getEyesCount() + "</eyesCount>");
                    writer.println("      <toothCount>" + d.getHead().getToothCount() + "</toothCount>");
                    writer.println("    </head>");
                } else {
                    writer.println("    <head>null</head>");
                }
                writer.println("  </dragon>");
            }
            writer.println("</dragons>");
            System.out.println("Коллекция сохранена в файл: " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка: Нет прав доступа на запись в файл " + fileName);
        }
    }
}