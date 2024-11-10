package main;

import helpers.Display;
import helpers.FileUtils;
import helpers.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
            Display.displayMessage("No enrolled courses found for student ID: " + studentId);
        } else {
            Display.displayMessage("\nEnrolled Courses:");
            for (Course course : enrolledCourses) {
                Display.displayMessage(course.getId() + ": " + course.getName() + " Instructor: " + course.getInstructorId());
            }
        }
    }

    public static boolean checkPrerequisites(String studentId, Course course) {
        List<String> prerequisites = course.getPrerequisites();

        if (prerequisites.isEmpty() || (prerequisites.size() == 1 && prerequisites.get(0).isEmpty())) {
            return true;
        }

        List<Enrollment> enrollments = loadEnrollments();
        List<String> enrolledCourseIds = new ArrayList<>();

        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudentId().equals(studentId)) {
                enrolledCourseIds.add(enrollment.getCourseId());
            }
        }

        for (String prereq : prerequisites) {
            if (!enrolledCourseIds.contains(prereq)) {
                Display.displayMessage("Missing prerequisite course: " + prereq);
                return false;
            }
        }
        return true;
    }
    public static void dropCourse(String studentId) {
        List<Enrollment> enrollments = loadEnrollments();
        Enrollment toDrop = null;
        String courseId = Utils.getInput("Select a course ID to drop: ");

        // Check if the student is enrolled in the course
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudentId().equals(studentId) && enrollment.getCourseId().equals(courseId)) {
                toDrop = enrollment;
                Display.displayMessage(toDrop.toString());
                break;
            }
        }

        if (toDrop == null) {
            Display.displayMessage("Error: You are not enrolled in this course.");
            return;
        }

        // Check if drop period is open
        if (!isDropPeriodOpen()) {
            Display.displayMessage("Error: Drop period is closed. Unable to drop the course.");
            return;
        }
            boolean confirmed = getUserConfirmation();
            if (!confirmed) {
                Display.displayMessage("Course drop canceled.");
                return;
            }

        enrollments.remove(toDrop);
        for (Enrollment enrollment : enrollments) {
            saveEnrollment(enrollment);
        }       
        Display.displayMessage("Course dropped successfully. Your updated schedule has been saved.");
    }

    private static boolean isDropPeriodOpen() {
        // Placeholder 
        return true; 
    }

    private static boolean getUserConfirmation() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            Display.displayMessage("Do you want to proceed with dropping the course? (yes/no): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes")) {
                return true;
            } else if (input.equals("no")) {
                return false;
            } else {
                Display.displayMessage("Invalid input. Please enter 'yes' or 'no'.");
            }
        }
    }
}
