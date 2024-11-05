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

    public String getStudentId() { return studentId; }
    public String getCourseId() { return courseId; }

    @Override
    public String toString() {
        return studentId + "," + courseId;
    }

    public static List<Enrollment> loadEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        List<String> lines = FileUtils.readFromFile("", "enrollments.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            enrollments.add(new Enrollment(data[0], data[1]));
        }
        return enrollments;
    }

    public static void saveEnrollment(Enrollment enrollment) {
        FileUtils.writeToFile("", "enrollments.txt", enrollment.toString());
    }

    public static void displayAllEnrolledCourses(String studentId) {
        List<Enrollment> enrollments = loadEnrollments();
        List<Course> enrolledCourses = new ArrayList<>();

        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudentId().equals(studentId)) {
                Course course = Course.findCourseById(enrollment.getCourseId());
                if (course != null) {
                    enrolledCourses.add(course);
                }
            }
        }

        if (enrolledCourses.isEmpty()) {
            System.out.println("No enrolled courses found for student ID: " + studentId);
        } else {
            System.out.println("\nEnrolled Courses:");
            for (Course course : enrolledCourses) {
                System.out.println(course.getId() + ": " + course.getName() + " Instructor: " + course.getInstructorId());
            }
        }
    }
}
