package helpers;

import main.Course;
import users.DataAccess;
import users.Instructor;
import users.Student;
import main.Enrollment;



public class Display {



    public static void displayStudentMenu() {
        String id = Utils.getInput("Enter Student ID or 'new' to create an account: ");
        Student student = DataAccess.findStudentById(id);
        if (id.equalsIgnoreCase("new")) {
            displayMessage("LOL");
        } else {
            student = Student.findStudentById(id);
            if (student == null) {
                displayMessage("Student not found.");
                return;
            }
        }

        while (true) {
            displayMessage("\nStudent Menu:");
            displayMessage("1 View my Courses");
            displayMessage("2 Enroll in Course");
            displayMessage("3 Submit Assignment");
            displayMessage("4 Drop a course");
            displayMessage("5 Logout\n");
            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                	Course.displayAllCourses();
                    break;
                case "2":
                    assert student != null;
                    Course.displayAllCourses();
                    student.enrollInCourse();
                    break;
                case "3":
                    assert student != null;
                    student.submitAssignment();
                    break;
                case "4":
                	Course.displayAllCourses();
                	Enrollment.dropCourse(student.id);
                	break;
                case "5":
                    return;
                default:
                    displayMessage("Bad Baka");
            }
        }
    }

    public static void displayInstructorMenu() {
        String id = Utils.getInput("Enter Instructor ID or 'new' to create an account: ");
        Instructor instructor = DataAccess.findInstructorById(id);
        if (id.equalsIgnoreCase("new")) {
            displayMessage("lol");
        } else {
            instructor = Instructor.findInstructorById(id);
            if (instructor == null) {
                displayMessage("Instructor not found.");
                return;
            }
        }

        while (true) {
            displayMessage("\nInstructor Menu:");
            displayMessage("1 Create Assignment");
            displayMessage("2 Grade Assignments");
            displayMessage("3 Logout");
            String choice = Utils.getInput("Select an option: \n");

            switch (choice) {
                case "1":
                    //create assigment
                    break;
                case "2":
                    instructor.gradeAssignments();
                    break;
                case "3":
                    return;
                default:
                    displayMessage("Bad Baka");
            }
        }
    }

    public static void displayMessage(String message) {
        System.out.println(message);
    }

}
