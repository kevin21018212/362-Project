package main;

import helpers.Display;
import helpers.FileUtils;
import helpers.Utils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Enrollment {
    private static final String[] HEADERS = {"StudentId", "CourseId"};
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
        return studentId + "::" + courseId;
    }

    public static List<Enrollment> loadEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        List<String[]> lines = FileUtils.readStructuredData("", "enrollments.txt");

        for (String[] data : lines) {
            if (data.length >= 2) {
                enrollments.add(new Enrollment(data[0], data[1]));
            }
        }
        return enrollments;
    }

    public static void saveEnrollment(Enrollment enrollment) {
        List<String[]> existingData = FileUtils.readStructuredData("", "enrollments.txt");
        List<String[]> newData = new ArrayList<>(existingData);
        newData.add(new String[]{enrollment.getStudentId(), enrollment.getCourseId()});

        FileUtils.writeStructuredData("", "enrollments.txt", HEADERS, newData);
    }

    public static void saveAllEnrollments(List<Enrollment> enrollments) {
        List<String[]> data = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            data.add(new String[]{enrollment.getStudentId(), enrollment.getCourseId()});
        }
        FileUtils.writeStructuredData("", "enrollments.txt", HEADERS, data);
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

    public static List<String> getEnrolledCourses(String studentId) {
        List<String> enrolledCourses = new ArrayList<>();
        List<Enrollment> enrollments = Enrollment.loadEnrollments();

        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudentId().equals(studentId)) {
                enrolledCourses.add(enrollment.getCourseId());
            }
        }
        return enrolledCourses;
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
        String courseId = Utils.getInput("Select a course ID to drop: ").toUpperCase();

        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudentId().equals(studentId) && enrollment.getCourseId().equals(courseId)) {
                toDrop = enrollment;
                break;
            }
        }

        if (toDrop == null) {
            Display.displayMessage("Error: You are not enrolled in this course.");
            return;
        }

        if (!isDropPeriodOpen()) {
            Display.displayMessage("Error: Drop period is closed. Unable to drop the course.");
            return;
        }

        if (getUserConfirmation(courseId)) {
            enrollments.remove(toDrop);
            saveAllEnrollments(enrollments);
            Display.displayMessage("Course dropped successfully. Your updated schedule has been saved.");
        } else {
            Display.displayMessage("Course drop canceled.");
        }
    }

    private static boolean isDropPeriodOpen() {
        return true; // Placeholder implementation
    }

    private static boolean getUserConfirmation(String course) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            Display.displayMessage("Do you want to proceed with dropping " + course + " (yes/no): ");
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

    /**
     * Retrieves the grade for this enrollment.
     * @return the grade as a double, or -1 if no grade is found.
     */
    public double getGrade() {
        String filePath = "src/data/grades.txt"; // Path to the grades file

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String studentId = data[0].trim();
                    String courseId = data[1].trim();
                    double grade = Double.parseDouble(data[2].trim());

                    // Check if the grade matches this enrollment's student and course IDs
                    if (studentId.equals(this.studentId) && courseId.equals(this.courseId)) {
                        return grade;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Return -1 if no grade is found
        return -1;
    }
}