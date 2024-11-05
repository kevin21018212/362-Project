import helpers.FileUtils;
import helpers.User;
import helpers.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Instructor extends User {

    public Instructor(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public void displayMenu() {
        while (true) {
            System.out.println("\nInstructor Menu:");
            System.out.println("1. Create Assignment");
            System.out.println("2. Grade Assignments");
            System.out.println("3. Logout");
            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    createAssignment();
                    break;
                case "2":
                    gradeAssignments();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void createAssignment() {
        String courseId = Utils.getInput("Enter Course ID: ");
        Course course = Course.findCourseById(courseId);
        if (course != null && course.getInstructorId().equals(this.id)) {
            String assignmentId = Utils.generateId("A", FileUtils.getNextId("courses/" + courseId, "assignments.txt"));
            String title = Utils.getInput("Enter Assignment Title: ");
            String dueDate = Utils.getInput("Enter Due Date: ");
            Assignment assignment = new Assignment(assignmentId, title, dueDate, courseId);
            course.addAssignment(assignment);
            Utils.displayMessage("Assignment created.");
        } else {
            Utils.displayMessage("Course not found or you are not the instructor for this course.");
        }
    }

    private void gradeAssignments() {
        String courseId = Utils.getInput("Enter Course ID: ");
        Course course = Course.findCourseById(courseId);
        if (course != null && course.getInstructorId().equals(this.id)) {
            String directory = "courses/" + courseId;
            String fileName = "submissions.txt";
            List<String> submissions = FileUtils.readFromFile(directory, fileName);
            List<String> updatedSubmissions = new ArrayList<>();
            for (String submission : submissions) {
                String[] data = submission.split(",");
                if (data[2].equals("Not Graded")) {
                    String grade = Utils.getInput("Enter grade for Student ID " + data[0] + ", Assignment ID " + data[1] + ": ");
                    data[2] = grade;
                }
                updatedSubmissions.add(String.join(",", data));
            }
            FileUtils.OverwriteFile(directory, fileName, Collections.singletonList(updatedSubmissions));
            Utils.displayMessage("Assignments graded.");
        } else {
            Utils.displayMessage("Course not found or you are not the instructor for this course.");
        }
    }
}
