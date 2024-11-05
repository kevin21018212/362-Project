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
            System.out.println("Writing error: " + e.getMessage());
        }
    }

    //overwrite the entire file
    public static void writeToFileOverwrite(String fileName, List<String> dataLines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String data : dataLines) {
                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Writing error: " + e.getMessage());
        }
    }

    public static List<String> readFromFile(String fileName) {
        List<String> lines = new ArrayList<>();
        File file = new File(fileName);
        // doesn't exist, create
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("File creation error: " + e.getMessage());
            }
            return lines;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty())
                    lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Reading error: " + e.getMessage());
        }
        return lines;
    }

    public static int getNextID(String fileName) {
        List<String> lines = readFromFile(fileName);
        return lines.size() + 1;
    }
}
