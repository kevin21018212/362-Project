package users;

import helpers.FileUtils;
import helpers.User;
import helpers.Utils;
import main.Assignment;
import main.Course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Instructor extends User {

    public Instructor(String id, String name, String email) {
        super(id, name, email);
    }

    public static Instructor findInstructorById(String id) {
        return null;
    }


    private void createAssignment() {
        String courseId = Utils.getInput("Enter main.Course ID: ");
        Course course = Course.findCourseById(courseId);
        if (course != null && course.getInstructorId().equals(this.id)) {
            String assignmentId = Utils.generateId("A", FileUtils.getNextId("courses/" + courseId, "assignments.txt"));
            String title = Utils.getInput("Enter main.Assignment Title: ");
            String dueDate = Utils.getInput("Enter Due Date: ");
            Assignment assignment = new Assignment(assignmentId, title, dueDate, courseId);
            course.addAssignment(assignment);
            Utils.displayMessage("main.Assignment created.");
        } else {
            Utils.displayMessage("main.Course not found or you are not the instructor for this course.");
        }
    }

    public void gradeAssignments() {
        String courseId = Utils.getInput("Enter main.Course ID: ");
        Course course = Course.findCourseById(courseId);
        if (course != null && course.getInstructorId().equals(this.id)) {
            String directory = "courses/" + courseId;
            String fileName = "submissions.txt";
            List<String> submissions = FileUtils.readFromFile(directory, fileName);
            List<String> updatedSubmissions = new ArrayList<>();
            for (String submission : submissions) {
                String[] data = submission.split(",");
                if (data[2].equals("Not Graded")) {
                    String grade = Utils.getInput("Enter grade for Student ID " + data[0] + ", main.Assignment ID " + data[1] + ": ");
                    data[2] = grade;
                }
                updatedSubmissions.add(String.join(",", data));
            }
            FileUtils.OverwriteFile(directory, fileName, Collections.singletonList(updatedSubmissions));
            Utils.displayMessage("Assignments graded.");
        } else {
            Utils.displayMessage("main.Course not found or you are not the instructor for this course.");
        }
    }
}

