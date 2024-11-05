import helpers.FileUtils;
import helpers.User;
import helpers.Utils;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {

    public Student(String id, String name, String email) {
        super(id, name, email);
    }

    public String getId() {
        return super.id;
    }

    @Override
    public void displayMenu() {
        while (true) {
            System.out.println("\nStudent Menu:");
            System.out.println("1. View Available Courses");
            System.out.println("2. Enroll in a Course");
            System.out.println("3. Submit Assignment");
            System.out.println("4. Logout");
            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    Course.displayAllCourses();
                    break;
                case "2":
                    enrollInCourse();
                    break;
                case "3":
                    submitAssignment();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void enrollInCourse() {
        String courseId = Utils.getInput("Enter Course ID to enroll: ");
        Course course = Course.findCourseById(courseId);
        if (course != null) {
            Enrollment enrollment = new Enrollment(this.id, courseId);
            Enrollment.saveEnrollment(enrollment);
            Utils.displayMessage("Enrolled in course " + course.getName());
        } else {
            Utils.displayMessage("Course not found.");
        }
    }

    private void submitAssignment() {
        String courseId = Utils.getInput("Enter Course ID: ");
        Course course = Course.findCourseById(courseId);
        if (course != null) {
            String assignmentId = Utils.getInput("Enter Assignment ID: ");
            Assignment assignment = course.findAssignmentById(assignmentId);
            if (assignment != null) {
                String directory = "courses/" + courseId;
                String fileName = "submissions.txt";
                String data = this.id + "," + assignmentId + ",Not Graded";
                FileUtils.writeToFile(directory, fileName, data);
                Utils.displayMessage("Assignment submitted.");
            } else {
                Utils.displayMessage("Assignment not found.");
            }
        } else {
            Utils.displayMessage("Course not found.");
        }
    }

    // Load students from file
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        List<String> lines = FileUtils.readFromFile("", "students.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            students.add(new Student(data[0], data[1], data[2]));
        }
        return students;
    }

    public static Student findStudentById(String id) {
        List<Student> students = loadStudents();
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + email;
    }
}
