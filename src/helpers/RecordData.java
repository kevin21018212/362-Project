package helpers;

import java.io.*;
import java.util.*;

public class RecordData {
    public static final String SEPARATOR = "::";
    public static final String RECORD_END = "##";

    private String id;
    private Map<String, String> fields;

    public RecordData(String id) {
        this.id = id;
        this.fields = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void addField(String key, String value) {
        // Escape separators in values
        value = value.replace(SEPARATOR, "\\" + SEPARATOR)
                .replace(RECORD_END, "\\" + RECORD_END);
        fields.put(key, value);
    }

    public String getField(String key) {
        return fields.get(key);
    }

    public Map<String, String> getFields() {
        return new HashMap<>(fields);
    }

    public String toStorageFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(SEPARATOR);

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            sb.append(entry.getKey())
                    .append(SEPARATOR)
                    .append(entry.getValue())
                    .append(SEPARATOR);
        }

        return sb.toString();
    }

    public static RecordData fromStorageFormat(String data) {
        String[] parts = data.split(SEPARATOR);
        if (parts.length < 1) {
            throw new IllegalArgumentException("Invalid storage format");
        }

        RecordData record = new RecordData(parts[0]);

        for (int i = 1; i < parts.length - 1; i += 2) {
            if (i + 1 < parts.length) {
                String key = parts[i];
                String value = parts[i + 1]
                        .replace("\\" + SEPARATOR, SEPARATOR)
                        .replace("\\" + RECORD_END, RECORD_END);
                record.addField(key, value);
            }
        }

        return record;
    }
}