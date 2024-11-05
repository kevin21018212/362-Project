package helpers;

import helpers.Utils;
import users.Instructor;
import users.Student;

import java.util.List;

public class Display {

    public static void displayMainMenu() {
        while (true) {
            System.out.println("\nWelcome to the University System");
            System.out.println("1. Student Login");
            System.out.println("2. Instructor Login");
            System.out.println("3. Faculty Login");
            System.out.println("4. Exit");
            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    displayStudentMenu();
                    break;
                case "2":
                    displayInstructorMenu();
                    break;
                case "3":
                    displayFacultyMenu();
                    break;
                case "4":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public static void displayStudentMenu() {
        String id = Utils.getInput("Enter Student ID or 'new' to create an account: ");
        Student student = null;
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
            System.out.println("1. View Available Courses");
            System.out.println("2. Enroll in a main.Course");
            System.out.println("3. Submit main.Assignment");
            System.out.println("4. Logout");
            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    //display all curses
                    break;
                case "2":
                    student.enrollInCourse();
                    break;
                case "3":
                    student.submitAssignment();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public static void displayInstructorMenu() {
        String id = Utils.getInput("Enter Instructor ID or 'new' to create an account: ");
        Instructor instructor = null;
        if (id.equalsIgnoreCase("new")) {
            return
        } else {
            instructor = Instructor.findInstructorById(id);
            if (instructor == null) {
                System.out.println("Instructor not found.");
                return;
            }
        }

        while (true) {
            System.out.println("\nInstructor Menu:");
            System.out.println("1. Create main.Assignment");
            System.out.println("2. Grade Assignments");
            System.out.println("3. Logout");
            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    instructor.createAssignment();
                    break;
                case "2":
                    instructor.gradeAssignments();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public static void displayFacultyMenu() {
        String id = Utils.getInput("Enter Faculty ID or 'new' to create an account: ");
        Faculty faculty;
        if (id.equalsIgnoreCase("new")) {
             return;
        } else {
            faculty = Faculty.findFacultyById(id);
            if (faculty == null) {
                System.out.println("Faculty not found.");
                return;
            }
        }

        while (true) {
            System.out.println("\nFaculty Menu:");
            System.out.println("1. Create main.Course");
            System.out.println("2. Logout");
            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    faculty.createCourse();
                    break;
                case "2":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
