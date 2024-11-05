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
        String courseId = Utils.getInput("Enter Course ID: ");
        Course course = Course.findCourseById(courseId);
        if (course != null && course.getInstructorId().equals(this.id)) {
            String fileName = "courses/" + courseId + "/submissions.txt";

            System.out.println(fileName);
            List<String> submissions = FileUtils.readFromFile("", fileName);
            List<String> updatedSubmissions = new ArrayList<>();

            for (String submission : submissions) {
                String[] data = submission.split(",");
                if (data[3].equals("Not Graded")) {
                    String grade = Utils.getInput("Enter grade for Student ID " + data[2] + ", Assignment ID " + data[1] + ": ");
                    data[3] = grade;
                }
                updatedSubmissions.add(String.join(",", data));
            }

            FileUtils.OverwriteFile("", fileName, updatedSubmissions);
            Utils.displayMessage("Assignments graded.");
        } else {
            Utils.displayMessage("Course not found or you are not the instructor for this course.");
        }
    }

}

