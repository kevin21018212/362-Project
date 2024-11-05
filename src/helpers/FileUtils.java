package helpers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static void writeToFile(String fileName, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Writing error" + e.getMessage());
        }
    }

    public static List<String> readFromFile(String fileName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Reading error" + e.getMessage());
        }
        return lines;
    }
}
