package users;



import helpers.Display;
import helpers.User;
import helpers.Utils;

import main.Course;
import main.Enrollment;



public class Student extends User {

    public Student(String id, String name, String email) {
        super(id, name, email);
    }

    public void enrollInCourse() {
        String courseId = Utils.getInput("\nEnter Course ID to enroll: ");
        Course course = Course.findCourseById(courseId);

        if (course != null) {
            if (!Enrollment.checkPrerequisites(this.id, course)) {
                Display.displayMessage("Prerequisites not met for this course.");
                return;
            }

            if (course.isFull()) {
                Display.displayMessage("This course is full.");
                return;
            }

            for (Enrollment enrollment : Enrollment.loadEnrollments()) {
                if (enrollment.getStudentId().equals(this.id) && enrollment.getCourseId().equals(courseId)) {
                    Display.displayMessage("You are already enrolled in this course.");
                    return;
                }
            }

            Enrollment enrollment = new Enrollment(this.id, courseId);
            Enrollment.saveEnrollment(enrollment);
            course.incrementEnrollment();
            Display.displayMessage("Enrolled in course " + course.getName());
        } else {
            Display.displayMessage("Course not found.");
        }
    }
}








