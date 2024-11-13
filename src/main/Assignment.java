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