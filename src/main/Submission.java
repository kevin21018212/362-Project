package main;

import helpers.FileUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Submission {
    private static final String[] SUBMISSION_HEADERS = {
            "SubmissionId", "AssignmentId", "StudentId", "Grade", "SubmittedDate"
    };

    private final String id;
    private final String assignmentId;
    private final String studentId;
    private String grade;
    private final String submittedDate;

    public Submission(String id, String assignmentId, String studentId, String grade, String submittedDate) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.grade = grade;
        this.submittedDate = submittedDate;
    }

    public String getId() { return id; }
    public String getAssignmentId() { return assignmentId; }
    public String getStudentId() { return studentId; }
    public String getGrade() { return grade; }
    public String getSubmittedDate() { return submittedDate; }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public static List<Submission> loadSubmissions(String courseId, String assignmentId) {
        List<Submission> submissions = new ArrayList<>();
        String fileName = "submissions.txt";
        List<String[]> data = FileUtils.readStructuredData("courses/"+courseId, fileName);

        for (String[] fields : data) {

            String submissionId = fields[0].trim();
            String assignId = fields[1].trim();
            String studentId = fields[2].trim();
            String grade = fields[3].trim();
            String submittedDate = fields[4].replace("##", "").trim();

            // Only add submissions for the specified assignment
            if (assignId.equals(assignmentId)) {
                submissions.add(new Submission(submissionId, assignId, studentId, grade, submittedDate));
            }
        }
        return submissions;
    }

    public static boolean isSubmitted(String courseId, String studentId, String assignmentId) {
        String fileName = "courses/" + courseId + "/submissions.txt";
        List<String[]> data = FileUtils.readStructuredData("", fileName);

        for (String[] row : data) {
            if (row.length >= 3 && row[1].equals(assignmentId) && row[2].equals(studentId)) {
                return true;
            }
        }
        return false;
    }

    public static void submit(String courseId, String studentId, String assignmentId) {
        String submissionId = "sub_" + System.currentTimeMillis();
        String submittedDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        // Create submission data array
        String[] submissionData = {
                submissionId,
                assignmentId,
                studentId,
                "Not Graded",
                submittedDate
        };

        String fileName = "courses/" + courseId + "/submissions.txt";

        // Read existing submissions
        List<String[]> existingSubmissions = FileUtils.readStructuredData("", fileName);

        // Add new submission
        existingSubmissions.add(submissionData);

        // Write back to file with headers
        FileUtils.writeStructuredData("", fileName, SUBMISSION_HEADERS, existingSubmissions);
    }

    public List<Submission> getSubmissions(String courseId) {
        return Submission.loadSubmissions(courseId, this.id);
    }

    @Override
    public String toString() {
        return id + "::" + assignmentId + "::" + studentId + "::" + grade + "::" + submittedDate;
    }
}