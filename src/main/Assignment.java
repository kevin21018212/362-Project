package main;

import helpers.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class Assignment {
    private final String id;
    private final String title;
    private final String dueDate;
    private final String courseId;

    public Assignment(String id, String title, String dueDate, String courseId) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.courseId = courseId;
    }

    // Getters
    public String getId() { return id; }


    @Override
    public String toString() {
        return id + "," + title + "," + dueDate + "," + courseId;
    }

    // Load assignments for a specific course
    public static List<Assignment> loadAssignments(String courseId) {
        List<Assignment> assignments = new ArrayList<>();
        String directory = "data/courses/" + courseId;
        String fileName = "assignments.txt";
        List<String> lines = FileUtils.readFromFile(directory, fileName);
        for (String line : lines) {
            String[] data = line.split(",");
            assignments.add(new Assignment(data[0], data[1], data[2], data[3]));
        }
        return assignments;
    }

    // Find an assignment by ID in a specific course
    public static Assignment findAssignmentById(String courseId, String assignmentId) {
        List<Assignment> assignments = loadAssignments(courseId);
        for (Assignment assignment : assignments) {
            if (assignment.getId().equals(assignmentId)) {
                return assignment;
            }
        }
        return null;
    }
}
