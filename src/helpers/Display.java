package helpers;

import main.Course;
import users.DataAccess;
import users.Instructor;
import users.Registrar;
import users.Student;



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
            displayMessage("4 View or Change Major");
            displayMessage("5 Logout\n");
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
                    assert student != null;
                    student.changeMajor();
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

    public static void displayRegistrarMenu() {
        String id = Utils.getInput("Enter Registrar ID: ");
//        Registrar registrar = DataAccess.findRegistrarById(id);
        Registrar registrar = new Registrar("1", "registrar", "jd@em.com");

        if (registrar == null) {
            displayMessage("Registrar not found.");
            return;
        }

        while (true) {
            displayMessage("\nRegistrar Menu:");
            displayMessage("1. Register New Student");
            displayMessage("2. Generate Transcript");
            displayMessage("3. Logout\n");

            String choice = Utils.getInput("Select an option: ");
            switch (choice) {
                case "1":
                    registerNewStudent(registrar);
                    break;
                case "2":
                    String studentId = Utils.getInput("Enter student ID: ");
                    registrar.generateTranscript(studentId);
                    break;
                case "3":
                    return;
                default:
                    displayMessage("Invalid option");
            }
        }
    }

    private static void registerNewStudent(Registrar registrar) {
        displayMessage("\nNew Student Registration Form");
        String fullName = Utils.getInput("Enter full name: ");
        String dateOfBirth = Utils.getInput("Enter date of birth (YYYY-MM-DD): ");
        String contactInfo = Utils.getInput("Enter email: ");
        String address = Utils.getInput("Enter address: ");
        String programOfStudy = Utils.getInput("Enter program of study: ");
        String academicTerm = Utils.getInput("Enter academic term (e.g., FALL2024): ");

        registrar.enrollNewStudent(fullName, dateOfBirth, contactInfo,
                address, programOfStudy, academicTerm);
    }

    public static void displayMessage(String message) {
        System.out.println(message);
    }

}
