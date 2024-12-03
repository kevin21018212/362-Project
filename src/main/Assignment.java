package main;

import helpers.Display;
import helpers.FileUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    public String getTitle() {return title;}

    public static String generateAssignmentId(String rawId) {
        try {
            int numericId = Integer.parseInt(rawId);
            return "A" + numericId; // Format as "A{integer}"
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Assignment ID must be an integer.");
        }
    }

    public static List<Assignment> createAssignments(String courseId) {
        List<Assignment> assignments = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            Display.displayMessage("Enter Assignment ID (integer only, or type 'done' to finish):");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("done")) {
                break;
            }

            try {
                String formattedAssignmentId = generateAssignmentId(input);

                Display.displayMessage("Enter Assignment Due Date (YYYY-MM-DD):");
                String dueDate = scanner.nextLine().trim();

                assignments.add(new Assignment(formattedAssignmentId, courseId, dueDate));
                Display.displayMessage("Assignment added: " + formattedAssignmentId + " due on " + dueDate);
            } catch (IllegalArgumentException e) {
                Display.displayMessage("Invalid input. Assignment ID must be an integer.");
            }
        }

        return assignments;
    }



    public static List<Assignment> loadAssignments(String courseId) {
        List<Assignment> assignments = new ArrayList<>();
//        String fileName = "assignments.txt";
        String directory = "courses/" + courseId;
        List<String[]> data = FileUtils.readStructuredData(directory, "assignments.txt");

        for (String[] fields : data) {
//            String[] fields = row.split("::");

            String assignmentId = fields[0].trim();
            String courseid = fields[1].trim();
            String dueDate = fields[2].replace("##", "").trim();

            // Only add assignments for the specified course
            if (courseid.equals(courseId)) {
                assignments.add(new Assignment(assignmentId, courseid, dueDate));
            }
        }
        return assignments;
    }

    @Override
    public String toString() {
        return id + "::" + title + "::" + dueDate;
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