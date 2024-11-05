package main;

import helpers.FileUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public String getId() { return id; }
    public String getDueDate() { return dueDate; }

    @Override
    public String toString() {
        return "Assignment ID: " + id + ", Title: " + title + ", Due Date: " + dueDate;
    }

    public static List<Assignment> loadAssignments(String courseId) {
        List<Assignment> assignments = new ArrayList<>();
        String fileName = "courses/" + courseId + "/assignments.txt";
        FileUtils.readFromFile("", fileName).stream()
                .map(line -> line.split(","))
                .filter(data -> data.length == 3)
                .forEach(data -> assignments.add(new Assignment(data[0], data[1], data[2])));
        return assignments;
    }

    public static Assignment findAssignmentById(String courseId, String assignmentId) {
        List<Assignment> assignments = loadAssignments(courseId);
        for (Assignment assignment : assignments) {
            if (assignment.getId().equals(assignmentId)) {
                return assignment;
            }
        }
        return null;
    }

    public static List<String[]> getSubmissions(String courseId, String assignmentId) {
        String fileName = "courses/" + courseId + "/submissions.txt";
        List<String[]> submissions = new ArrayList<>();

        FileUtils.readFromFile("", fileName).stream()
                .map(line -> line.split(","))
                .filter(data -> data[1].equals(assignmentId))
                .forEach(submissions::add);

        return submissions;
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
        String submittedDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String data = submissionId + "," + assignmentId + "," + studentId + ",Not Graded," + submittedDate;
        String fileName = "courses/" + courseId + "/submissions.txt";
        FileUtils.writeToFile("", fileName, data);
    }
}
