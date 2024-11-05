package users;

import helpers.FileUtils;
import helpers.User;
import helpers.Utils;
import main.Assignment;
import main.Course;
import main.Enrollment;

import java.util.ArrayList;
import java.util.List;


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
        // Display all enrolled courses for the student
        Enrollment.displayAllEnrolledCourses(this.id);

        // Ask for course ID
        String courseId = Utils.getInput("Enter Course ID from the list above: ");
        Course course = Course.findCourseById(courseId);
        if (course == null) {
            Utils.displayMessage("Invalid Course ID.");
            return;
        }

        // Load assignments and display those not yet submitted
        List<Assignment> assignments = Assignment.loadAssignments(courseId);
        boolean hasUnsubmittedAssignments = false;

        System.out.println("Assignments not yet submitted:");
        for (Assignment assignment : assignments) {
            if (!Assignment.isSubmitted(courseId, this.id, assignment.getId())) {
                System.out.println(assignment);
                hasUnsubmittedAssignments = true;
            }
        }

        if (!hasUnsubmittedAssignments) {
            Utils.displayMessage("All assignments have been submitted for this course.");
            return;
        }

        // Ask for assignment ID
        String assignmentId = Utils.getInput("Enter Assignment ID: ");
        if (Assignment.isSubmitted(courseId, this.id, assignmentId)) {
            Utils.displayMessage("This assignment has already been submitted.");
        } else {
            Assignment.submit(courseId, this.id, assignmentId);
            Utils.displayMessage("Assignment submitted successfully.");
        }
    }






    public static Student findStudentById(String id) {
        return DataAccess.findStudentById(id);
    }

}

