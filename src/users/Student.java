package users;

import helpers.FileUtils;
import helpers.User;
import helpers.Utils;
import main.Assignment;
import main.Course;
import main.Enrollment;


public class Student extends User {

    public Student(String id, String name, String email) {
        super(id, name, email);
    }



    public void enrollInCourse() {
        String courseId = Utils.getInput("Enter Course ID to enroll: ");
        Course course = Course.findCourseById(courseId);

        if (course != null) {
            for (Enrollment enrollment : Enrollment.loadEnrollments()) {
                if (enrollment.getStudentId().equals(this.id) && enrollment.getCourseId().equals(courseId)) {
                    Utils.displayMessage("You are already enrolled in this course.");
                    return;
                }
            }
            Enrollment enrollment = new Enrollment(this.id, courseId);
            Enrollment.saveEnrollment(enrollment);
            Utils.displayMessage("Enrolled in course " + course.getName());
        } else {
            Utils.displayMessage("Course not found.");
        }
    }



    public void submitAssignment() {
        String courseId = Utils.getInput("Enter Course ID: ");
        Course course = Course.findCourseById(courseId);
        if (course != null) {
            String assignmentId = Utils.getInput("Enter Assignment ID: ");
            Assignment assignment = Assignment.findAssignmentById(courseId, assignmentId);
            if (assignment != null) {
                String fileName = "submissions.txt";
                String data = this.id + "," + assignmentId + ",Not Graded";
                FileUtils.writeToFile("", fileName, data);
                Utils.displayMessage("Assignment submitted.");
            } else {
                Utils.displayMessage("Assignment not found.");
            }
        } else {
            Utils.displayMessage("Course not found.");
        }
    }


    public static Student findStudentById(String id) {
        return DataAccess.findStudentById(id);
    }

}

