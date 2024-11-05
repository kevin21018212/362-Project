package main;

import helpers.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class Assignment {
    private final String id;
    private final String title;
    private final String dueDate;


    public Assignment(String id, String title, String dueDate) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;

    }

    // Getters
    public String getId() { return id; }


    @Override
    public String toString() {
        return id + "," + title + "," + dueDate + ",";
    }

    // Load assignments for a specific course
    public static List<Assignment> loadAssignments(String courseId) {
        List<Assignment> assignments = new ArrayList<>();
        String fileName = "courses/" + courseId + "/assignments.txt";

        FileUtils.readFromFile("", fileName).stream()
                .map(line -> line.split(","))
                .filter(data -> data.length == 3)
                .forEach(data -> assignments.add(new Assignment(data[0], data[1], data[2])));
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


    public static boolean isSubmitted(String courseId, String studentId, String assignmentId) {
        String fileName = "courses/" + courseId + "/submissions.txt";
        List<String> submissions = FileUtils.readFromFile("", fileName);

        for (String submission : submissions) {
            String[] data = submission.split(",");
            if (data[1].equals(assignmentId) && data[2].equals(studentId)) {
                return true;
            }
        }
        return false;
    }


    public static void submit(String courseId, String studentId, String assignmentId) {
        String submissionId = "sub_" + System.currentTimeMillis();
        String data = submissionId + "," + assignmentId + "," + studentId + ",Not Graded";
        String fileName = "courses/" + courseId + "/submissions.txt";
        FileUtils.writeToFile("", fileName, data);
    }
}
