package helpers;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class ReadAndWriteResponse {
    public void writeResponseToFile(String fileName, String response) {
        try {
            Path file = Path.of(fileName);
            Files.write(file, Collections.singleton(response));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLastAttributeTextContent(String fileName, String attr) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(fileName));
            NodeList nList = doc.getElementsByTagName(attr);
            String y = "";
            for (int i = 0, size = nList.getLength(); i < size; i++) {
                y = nList.item(i).getTextContent();
            }
            return y;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteFile(String fileName) {
        Path file = Path.of(fileName);
        try{
            Files.deleteIfExists(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
