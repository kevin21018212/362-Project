package helpers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {


    public static void writeToFile(String directory, String fileName, String data) {
        try {
            File dir = new File("src/data/" + directory);

            File file = new File(dir, fileName);

            // Ensure file ends with a newline
            if (file.exists() && file.length() > 0) {
                try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
                    raf.seek(file.length() - 1);
                    if (raf.readByte() != '\n') {
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                            writer.newLine();
                        }
                    }
                }
            }

            // Append new data
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Writing error: " + e.getMessage());
        }
    }



    public static void OverwriteFile(String directory, String fileName, List<String> dataLines) {
        try {
            // Ensure directory exists
            File dir = new File("src/data/" + directory);

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
            File file = new File("src/data/" + directory, fileName);
            if (!file.exists()) {
                return lines;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Reading error: " + e.getMessage());
        }
        return lines;
    }

}
