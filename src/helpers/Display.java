package helpers;

import main.Course;
import users.DataAccess;
import users.Instructor;
import users.Registrar;
import users.Student;
import main.Enrollment;

import static helpers.displays.MessageDisplay.displayMessageMenu;


public class Display {
    public static void displayStudentMenu() {
        String id = Utils.getInput("Enter Student ID to login. Please visit the Registrar menu to create an account: ");
        Student student = DataAccess.findStudentById(id);
//        if (id.equalsIgnoreCase("new")) {
//            displayMessage("LOL");
//        } else {
//            student = Student.findStudentById(id);
            if (student == null) {
                displayMessage("Student not found.");
                return;
            }
//        }

        while (true) {
            displayMessage("\nStudent Menu:");
            displayMessage("1 View my Courses");
            displayMessage("2 Enroll in Course");
            displayMessage("3 Submit Assignment");
            displayMessage("4 View or Change Major");
            displayMessage("5 View Grades");
            displayMessage("6 Apply for Graduation");
            displayMessage("7 Drop a class");
            displayMessage("8 View University Bill");
            displayMessage("69 View Messages");
            displayMessage("9 Logout\n");
            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    student.viewCourses();
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
                    student.changeMajorDisplay();
                    break;
                case "5":
                    assert student != null;
                    student.viewGrades();
                    break;
                case "6":
                    assert student != null;
                    student.applyForGraduation();
                    break;
                case "7":
                    assert student != null;
                    Enrollment.dropCourse(student.id);
                	break;
                case "8":
                    assert student != null;
                    student.viewUniversityBillingOptions();
                    //student.viewUniversityBill();
                    break;
                case "69":
                    assert student != null;
                    displayMessageMenu(student.id, student.name);
                    break;
                case "9":
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
            displayMessage("1 View Courses");
            displayMessage("2 Grade Assignments");
            displayMessage("69 View Messages");
            displayMessage("3 Logout");
            String choice = Utils.getInput("Select an option: \n");

            switch (choice) {
                case "1":
                    instructor.viewCourses();
                    break;
                case "2":
                    instructor.gradeAssignments();
                    break;
                case "69":
                    displayMessageMenu(instructor.id, instructor.name);
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
        registrar.displayAllStudents();
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