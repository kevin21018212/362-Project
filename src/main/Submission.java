package main;

import helpers.FileUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Submission {
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
        String fileName = "courses/" + courseId + "/submissions.txt";

        FileUtils.readFromFile("", fileName).stream()
                .map(line -> line.split(","))
                .filter(data -> data.length >= 5 && data[1].equals(assignmentId))
                .forEach(data -> submissions.add(new Submission(
                        data[0], data[1], data[2], data[3], data[4]
                )));

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

    public List<Submission> getSubmissions(String courseId) {
        return Submission.loadSubmissions(courseId, this.id);
    }

    @Override
    public String toString() {
        return "Submission ID: " + id + ", Assignment ID: " + assignmentId + ", Student ID: " + studentId +
                ", Grade: " + grade + ", Submitted Date: " + submittedDate;
    }
}
