package helpers;

import main.Course;
import users.DataAccess;
import users.Instructor;
import users.Student;



public class Display {



    public static void displayStudentMenu() {
        String id = Utils.getInput("Enter Student ID or 'new' to create an account: ");
        Student student = DataAccess.findStudentById(id);
        if (id.equalsIgnoreCase("new")) {
            System.out.println("LOL");
        } else {
            student = Student.findStudentById(id);
            if (student == null) {
                System.out.println("Student not found.");
                return;
            }
        }

        while (true) {
            System.out.println("\nStudent Menu:");
            System.out.println("1 View my Courses");
            System.out.println("2 Enroll in Course");
            System.out.println("3 Submit Assignment");
            System.out.println("4 Logout\n");
            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":

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
                    return;
                default:
                    System.out.println("Bad Baka");
            }
        }
    }

    public static void displayInstructorMenu() {
        String id = Utils.getInput("Enter Instructor ID or 'new' to create an account: ");
        Instructor instructor = DataAccess.findInstructorById(id);
        if (id.equalsIgnoreCase("new")) {
            System.out.println("lol");
        } else {
            instructor = Instructor.findInstructorById(id);
            if (instructor == null) {
                System.out.println("Instructor not found.");
                return;
            }
        }

        while (true) {
            System.out.println("\nInstructor Menu:");
            System.out.println("1 Create Assignment");
            System.out.println("2 Grade Assignments");
            System.out.println("3 Logout");
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
                    System.out.println("Bad Baka");
            }
        }
    }


}
