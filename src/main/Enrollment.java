package main;

import helpers.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class Enrollment {
    private String studentId;
    private String courseId;

    public Enrollment(String studentId, String courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    // Getters
    public String getStudentId() { return studentId; }
    public String getCourseId() { return courseId; }

    @Override
    public String toString() {
        return studentId + "," + courseId;
    }

    // Load enrollments
    public static List<Enrollment> loadEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        List<String> lines = FileUtils.readFromFile("", "enrollments.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            enrollments.add(new Enrollment(data[0], data[1]));
        }
        return enrollments;
    }

    // Save enrollment
    public static void saveEnrollment(Enrollment enrollment) {
        FileUtils.writeToFile("", "enrollments.txt", enrollment.toString());
    }
}
