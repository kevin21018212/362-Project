package helpers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    // Write data to a file (appending), ensuring directories exist
    public static void writeToFile(String directory, String fileName, String data) {
        try {
            // Ensure directory exists
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // Open file in append mode
            File file = new File(dir, fileName);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Writing error: " + e.getMessage());
        }
    }

    // Overwrite the entire file with dataLines
    public static void writeToFileOverwrite(String directory, String fileName, List<String> dataLines) {
        try {
            // Ensure directory exists
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String data : dataLines) {
                    writer.write(data);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Writing error: " + e.getMessage());
        }
    }

    // Read from file and return list of lines
    public static List<String> readFromFile(String directory, String fileName) {
        List<String> lines = new ArrayList<>();
        try {
            File file = new File(directory, fileName);
            // If file doesn't exist, return empty list
            if (!file.exists()) {
                return lines;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty())
                        lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Reading error: " + e.getMessage());
        }
        return lines;
    }

    // Get next ID for auto-increment within a directory
    public static int getNextId(String directory, String fileName) {
        List<String> lines = readFromFile(directory, fileName);
        return lines.size() + 1;
    }
}
