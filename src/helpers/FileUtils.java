package helpers;

import main.Assignment;

import java.io.*;
import java.util.*;

/**
 * Utility class for handling file operations in the course management system.
 * Provides methods for reading and writing structured data to files with support
 * for arrays and custom delimiters.
 */
public class FileUtils {
    /** Base directory path for all data files */
    private static final String BASE_PATH = "src/data/";

    /** Delimiter used to separate fields in the data files */
    private static final String DELIMITER = "::";

    /** Marker used to indicate the end of a data row */
    private static final String ROW_END = "##";

    /** Character marking the start of an array in the data */
    private static final String ARRAY_START = "[";

    /** Character marking the end of an array in the data */
    private static final String ARRAY_END = "]";

    /** Separator used between array elements */
    private static final String ARRAY_SEPARATOR = ", ";

    /**
     * Writes structured data to a file with support for arrays.
     *
     * @param directory The subdirectory within BASE_PATH where the file should be written
     * @param fileName The name of the file to write to
     * @param headers Array of column headers for the data
     * @param data List of string arrays containing the data to write
     *
     * File Format:
     * - Headers are written on the first line, separated by DELIMITER
     * - Each data row is written on a new line
     * - Fields within a row are separated by DELIMITER
     * - Each row ends with ROW_END
     * - Arrays are preserved with their brackets
     */
    public static void writeStructuredData(String directory, String fileName, String[] headers, List<String[]> data) {
        try {
            // Create directory if it doesn't exist
            File dir = new File(BASE_PATH + directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, fileName);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Write headers with delimiter and row end marker
                writer.write(String.join(DELIMITER, headers) + ROW_END);
                writer.newLine();

                // Process each data row
                for (String[] row : data) {
                    StringBuilder rowBuilder = new StringBuilder();
                    for (int i = 0; i < row.length; i++) {
                        String value = row[i];
                        // Preserve array format if value is an array
                        if (value.startsWith(ARRAY_START) && value.endsWith(ARRAY_END)) {
                            rowBuilder.append(value);
                        } else {
                            rowBuilder.append(value);
                        }
                        // Add delimiter between fields (except for last field)
                        if (i < row.length - 1) {
                            rowBuilder.append(DELIMITER);
                        }
                    }
                    // Add row end marker and write line
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
     * Reads structured data from a file and returns it as a List of String arrays.
     * Handles special formatting including arrays and delimiters.
     *
     * @param directory The subdirectory within BASE_PATH where the file is located
     * @param fileName The name of the file to read from
     * @return List<String[]> containing the parsed data (excluding headers)
     *
     * Reading Process:
     * - Skips the header line (first line)
     * - Handles nested arrays by tracking array boundaries
     * - Splits fields based on DELIMITER
     * - Removes ROW_END markers
     * - Preserves array structure in the data
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
                        // Remove row end marker
                        String cleanLine = line.replace(ROW_END, "");
                        if (!firstLine) {
                            // Parse data row
                            List<String> rowValues = new ArrayList<>();
                            StringBuilder currentValue = new StringBuilder();
                            boolean insideArray = false;

                            // Process each character to handle arrays and delimiters
                            for (char c : cleanLine.toCharArray()) {
                                if (c == '[') {
                                    insideArray = true;
                                    currentValue.append(c);
                                } else if (c == ']') {
                                    insideArray = false;
                                    currentValue.append(c);
                                    rowValues.add(currentValue.toString());
                                    currentValue = new StringBuilder();
                                } else if (c == ':' && !insideArray &&
                                        cleanLine.charAt(cleanLine.indexOf(c) + 1) == ':') {
                                    if (currentValue.length() > 0) {
                                        rowValues.add(currentValue.toString());
                                        currentValue = new StringBuilder();
                                    }
                                } else {
                                    currentValue.append(c);
                                }
                            }

                            // Add any remaining value
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

    /**
     * Creates a directory structure for a course and initializes required files.
     *
     * @param courseId The ID of the course (used for folder name and file paths)
     * @param initialAssignments List of assignments to be written in assignments.txt
     *                           If empty, assignments.txt will still be created but without data.
     */


    public static void initializeCourseFiles(String courseId, List<Assignment> initialAssignments) {
        String courseDirPath = BASE_PATH + "courses/" + courseId;
        File courseDir = new File(courseDirPath);

        try {
            // Create course directory if it does not exist
            if (!courseDir.exists()) {
                courseDir.mkdirs();
            }

            // Create assignments.txt and populate it with initial assignments if provided
            File assignmentsFile = new File(courseDir, "assignments.txt");
            if (assignmentsFile.createNewFile() && initialAssignments != null && !initialAssignments.isEmpty()) {
                String[] headers = {"AssignmentID", "AssignmentName", "DueDate"};

                // Convert initialAssignments to a List<String[]> for writeStructuredData
                List<String[]> assignmentData = new ArrayList<>();
                for (Assignment assignment : initialAssignments) {
                    assignmentData.add(new String[]{assignment.getId(), assignment.getTitle(), assignment.getDueDate()});
                }

                writeStructuredData("courses/" + courseId, "assignments.txt", headers, assignmentData);

            } else if (initialAssignments == null || initialAssignments.isEmpty()) {
                writeStructuredData("courses/" + courseId, "assignments.txt", new String[]{"AssignmentID", "AssignmentName", "DueDate"}, new ArrayList<>());
            }

            // Create an empty submissions.txt file
            File submissionsFile = new File(courseDir, "submissions.txt");
            if (submissionsFile.createNewFile()) {
                writeStructuredData("courses/" + courseId, "submissions.txt", new String[]{"SubmissionID", "AssignmentID", "StudentID", "Grade", "SubmittedDate"}, new ArrayList<>());
            }

        } catch (IOException e) {
            System.err.println("Error initializing course files for " + courseId + ": " + e.getMessage());
        }
    }


}