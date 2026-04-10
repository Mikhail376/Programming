package server.manager;

import common.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Класс для работы с XML-файлом.
 * Использует DOM-парсер для чтения и Stream API для формирования данных при сохранении.
 */
public class FileManager {
    private static final Logger logger = LoggerFactory.getLogger(FileManager.class);
    private final String fileName;

    public FileManager(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Загружает коллекцию из XML-файла.
     */
    public Map<Long, Dragon> load() {
        TreeMap<Long, Dragon> collection = new TreeMap<>();
        File file = new File(fileName);

        if (!file.exists()) {
            logger.warn("Файл {} не найден. Будет создана пустая коллекция.", fileName);
            return collection;
        }

        if (!file.canRead()) {
            logger.error("Нет прав на чтение файла {}. Проверьте настройки доступа.", fileName);
            return collection;
        }

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(bis);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("dragon");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    try {
                        Dragon dragon = parseDragon((Element) node);
                        collection.put(dragon.getId(), dragon);
                    } catch (Exception e) {
                        logger.warn("Пропущен некорректный элемент в XML: {}", e.getMessage());
                    }
                }
            }
            logger.info("Из файла успешно загружено элементов: {}", collection.size());
        } catch (Exception e) {
            logger.error("Критическая ошибка при чтении XML: {}", e.getMessage());
        }
        return collection;
    }

    /**
     * Вспомогательный метод для парсинга дракона из элемента XML.
     */
    private Dragon parseDragon(Element el) {
        long id = Long.parseLong(getElementText(el, "id"));
        String name = getElementText(el, "name");

        Element coordEl = (Element) el.getElementsByTagName("coordinates").item(0);
        int x = Integer.parseInt(getElementText(coordEl, "x"));
        double y = Double.parseDouble(getElementText(coordEl, "y"));

        LocalDateTime date = LocalDateTime.parse(getElementText(el, "creationDate"));

        String ageStr = getElementText(el, "age");
        Long age = ageStr.equals("null") ? null : Long.parseLong(ageStr);

        double wingspan = Double.parseDouble(getElementText(el, "wingspan"));
        float weight = Float.parseFloat(getElementText(el, "weight"));
        DragonCharacter character = DragonCharacter.valueOf(getElementText(el, "character"));

        DragonHead head = null;
        NodeList headNodes = el.getElementsByTagName("head");
        if (headNodes.getLength() > 0 && !headNodes.item(0).getTextContent().equals("null")) {
            Element hEl = (Element) headNodes.item(0);
            head = new DragonHead(
                    Long.parseLong(getElementText(hEl, "size")),
                    Long.parseLong(getElementText(hEl, "eyesCount")),
                    Double.parseDouble(getElementText(hEl, "toothCount"))
            );
        }

        return new Dragon(id, name, new Coordinates(x, y), date, age, wingspan, weight, character, head);
    }

    private String getElementText(Element parent, String tagName) {
        return parent.getElementsByTagName(tagName).item(0).getTextContent();
    }

    /**
     * Сохраняет коллекцию в XML-файл.
     * Реализовано с использованием Stream API для генерации данных.
     */
    public void save(Map<Long, Dragon> collection) {
        if (fileName == null || fileName.isEmpty()) {
            logger.error("Имя файла для сохранения не задано!");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<dragons>\n");

            // Использование Stream API для формирования строк объектов
            String xmlContent = collection.values().stream()
                    .map(this::convertToXml)
                    .collect(Collectors.joining());

            writer.write(xmlContent);
            writer.write("</dragons>");

            logger.info("Коллекция успешно сохранена в файл: {}", fileName);
        } catch (IOException e) {
            logger.error("Ошибка при записи в файл {}: {}", fileName, e.getMessage());
        }
    }

    /**
     * Преобразует объект Dragon в XML-представление.
     */
    private String convertToXml(Dragon d) {
        StringBuilder sb = new StringBuilder();
        sb.append("  <dragon>\n");
        sb.append("    <id>").append(d.getId()).append("</id>\n");
        sb.append("    <name>").append(d.getName()).append("</name>\n");
        sb.append("    <coordinates>\n");
        sb.append("      <x>").append(d.getCoordinates().getX()).append("</x>\n");
        sb.append("      <y>").append(d.getCoordinates().getY()).append("</y>\n");
        sb.append("    </coordinates>\n");
        sb.append("    <creationDate>").append(d.getCreationDate()).append("</creationDate>\n");
        sb.append("    <age>").append(d.getAge() == null ? "null" : d.getAge()).append("</age>\n");
        sb.append("    <wingspan>").append(d.getWingspan()).append("</wingspan>\n");
        sb.append("    <weight>").append(d.getWeight()).append("</weight>\n");
        sb.append("    <character>").append(d.getCharacter()).append("</character>\n");

        if (d.getHead() != null) {
            sb.append("    <head>\n");
            sb.append("      <size>").append(d.getHead().getSize()).append("</size>\n");
            sb.append("      <eyesCount>").append(d.getHead().getEyesCount()).append("</eyesCount>\n");
            sb.append("      <toothCount>").append(d.getHead().getToothCount()).append("</toothCount>\n");
            sb.append("    </head>\n");
        } else {
            sb.append("    <head>null</head>\n");
        }
        sb.append("  </dragon>\n");
        return sb.toString();
    }
}