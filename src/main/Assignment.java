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

    public static List<String[]> getSubmissions(String courseId, String assignmentId) {
        List<Submission> submissions = Submission.loadSubmissions(courseId, assignmentId);
        List<String[]> submissionData = new ArrayList<>();

        for (Submission submission : submissions) {
            String[] data = {
                    submission.getId(),
                    submission.getAssignmentId(),
                    submission.getStudentId(),
                    submission.getGrade(),
                    submission.getSubmittedDate()
            };
            submissionData.add(data);
        }

        return submissionData;
    }
}