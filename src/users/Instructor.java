package users;

import helpers.FileUtils;
import helpers.User;
import helpers.Utils;
import main.Course;

import java.util.ArrayList;
import java.util.List;

public class Instructor extends User {

    public Instructor(String id, String name, String email) {
        super(id, name, email);
    }

    public static Instructor findInstructorById(String id) {
        return DataAccess.findInstructorById(id);
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
            FileUtils.OverwriteFile(directory, fileName, updatedSubmissions);
            Utils.displayMessage("Assignments graded.");
        } else {
            Utils.displayMessage("main.Course not found or you are not the instructor for this course.");
        }
    }
}

