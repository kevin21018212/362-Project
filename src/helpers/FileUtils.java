package helpers;

import java.io.*;
import java.util.*;

public class FileUtils {
    private static final String BASE_PATH = "src/data/";
    private static final String DELIMITER = "::";
    private static final String ROW_END = "##";

    /**
     * Writes structured data with arrays
     */
    public static void writeStructuredData(String directory, String fileName, String[] headers, List<String[]> data) {
        try {
            File dir = new File(BASE_PATH + directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, fileName);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Write headers
                writer.write(String.join(DELIMITER, headers) + ROW_END);
                writer.newLine();

                // Write data rows
                for (String[] row : data) {
                    writer.write(String.join(DELIMITER, row) + ROW_END);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing data: " + e.getMessage());
        }
    }

    /**
     * Reads structured data and returns as List<String[]>
     */
    public static List<String[]> readStructuredData(String directory, String fileName) {
        List<String[]> data = new ArrayList<>();
        try {
            File file = new File(BASE_PATH + directory, fileName);
            if (!file.exists()) {
                return data;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean firstLine = true;

                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        // Remove row end marker and split by delimiter
                        String cleanLine = line.replace(ROW_END, "");
                        if (!firstLine) { // Skip header line
                            data.add(cleanLine.split(DELIMITER));
                        }
                        firstLine = false;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading data: " + e.getMessage());
        }
        return data;
    }

    /**
     * Gets headers from structured data file
     */
    public static String[] readHeaders(String directory, String fileName) {
        try {
            File file = new File(BASE_PATH + directory, fileName);
            if (!file.exists()) {
                return new String[0];
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                if (line != null) {
                    return line.replace(ROW_END, "").split(DELIMITER);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading headers: " + e.getMessage());
        }
        return new String[0];
    }

    /**
     * Appends a single row to existing file
     */
    public static void appendRow(String directory, String fileName, String[] rowData) {
        try {
            File file = new File(BASE_PATH + directory, fileName);
            if (!file.exists()) {
                throw new FileNotFoundException("File does not exist");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(String.join(DELIMITER, rowData) + ROW_END);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error appending row: " + e.getMessage());
        }
    }

    /**
     * Converts List<String> to List<String[]>
     */
    public static List<String[]> convertToStringArray(List<String> lines) {
        List<String[]> result = new ArrayList<>();
        for (String line : lines) {
            String cleanLine = line.replace(ROW_END, "");
            result.add(cleanLine.split(DELIMITER));
        }
        return result;
    }

    /**
     * Converts List<String[]> to List<String>
     */
    public static List<String> convertToString(List<String[]> data) {
        List<String> result = new ArrayList<>();
        for (String[] row : data) {
            result.add(String.join(DELIMITER, row) + ROW_END);
        }
        return result;
    }


}