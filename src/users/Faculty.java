package users;

import helpers.FileUtils;
import helpers.User;
import helpers.Utils;
import main.Course;

public class Faculty extends User {

    public Faculty(String id, String name, String email) {
        super(id, name, email);
    }



    private void createCourse() {
        String courseId = Utils.generateId("C", FileUtils.getNextId("", "courses.txt"));
        String name = Utils.getInput("Enter main.Course Name: ");
        String level = Utils.getInput("Enter main.Course Level: ");
        String instructorId = Utils.getInput("Enter Instructor ID: ");

        // Check if instructor exists
        Instructor instructor = Instructor.findInstructorById(instructorId);
        if (instructor != null) {
            Course course = new Course(courseId, name, level, instructorId);
            Course.saveCourse(course);
            Utils.displayMessage("main.Course created with ID " + courseId);
        } else {
            Utils.displayMessage("Instructor not found.");
        }
    }
}
