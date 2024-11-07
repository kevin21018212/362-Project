package helpers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private static final String DATA_DIR = "src/data/";

    public static void saveRecord(String filename, RecordData record) {
        try {
            File file = new File(DATA_DIR + filename);
            file.getParentFile().mkdirs();

            try (FileWriter writer = new FileWriter(file, true)) {
                String data = record.toStorageFormat();
                writer.write(data + RecordData.RECORD_END + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving record: " + e.getMessage(), e);
        }
    }

    public static List<RecordData> loadRecords(String filename) {
        List<RecordData> records = new ArrayList<>();
        File file = new File(DATA_DIR + filename);

        if (!file.exists()) {
            return records;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    // Remove RECORD_END before parsing
                    String recordData = line.substring(0,
                            line.length() - RecordData.RECORD_END.length());
                    records.add(RecordData.fromStorageFormat(recordData));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading records: " + e.getMessage(), e);
        }
        return records;
    }

    public static void saveRecords(String filename, List<RecordData> records) {
        try {
            File file = new File(DATA_DIR + filename);
            file.getParentFile().mkdirs();

            try (FileWriter writer = new FileWriter(file)) {
                for (RecordData record : records) {
                    writer.write(record.toStorageFormat() +
                            RecordData.RECORD_END + "\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving records: " + e.getMessage(), e);
        }
    }
}