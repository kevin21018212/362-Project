package helpers;

import java.io.*;
import java.util.*;

public class FileUtils {
    private static final String BASE_PATH = "src/data/";
    private static final String DELIMITER = "::";
    private static final String ROW_END = "##";
    private static final String ARRAY_START = "[";
    private static final String ARRAY_END = "]";
    private static final String ARRAY_SEPARATOR = ", ";

    /**
     * Writes structured data with support for arrays
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
                    StringBuilder rowBuilder = new StringBuilder();
                    for (int i = 0; i < row.length; i++) {
                        String value = row[i];
                        if (value.startsWith(ARRAY_START) && value.endsWith(ARRAY_END)) {
                            rowBuilder.append(value); // Keep array format intact
                        } else {
                            rowBuilder.append(value);
                        }
                        if (i < row.length - 1) {
                            rowBuilder.append(DELIMITER);
                        }
                    }
                    rowBuilder.append(ROW_END);
                    writer.write(rowBuilder.toString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing data: " + e.getMessage());
        }
    }

    /**
     * Reads structured data including arrays and returns as List<String[]>
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
                        String cleanLine = line.replace(ROW_END, "");
                        if (!firstLine) {
                            // Handle arrays in the data
                            List<String> rowValues = new ArrayList<>();
                            StringBuilder currentValue = new StringBuilder();
                            boolean insideArray = false;

                            for (char c : cleanLine.toCharArray()) {
                                if (c == '[') {
                                    insideArray = true;
                                    currentValue.append(c);
                                } else if (c == ']') {
                                    insideArray = false;
                                    currentValue.append(c);
                                    rowValues.add(currentValue.toString());
                                    currentValue = new StringBuilder();
                                } else if (c == ':' && !insideArray && cleanLine.charAt(cleanLine.indexOf(c) + 1) == ':') {
                                    if (currentValue.length() > 0) {
                                        rowValues.add(currentValue.toString());
                                        currentValue = new StringBuilder();
                                    }
                                } else {
                                    currentValue.append(c);
                                }
                            }

                            if (currentValue.length() > 0) {
                                rowValues.add(currentValue.toString());
                            }

                            data.add(rowValues.toArray(new String[0]));
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
    
}