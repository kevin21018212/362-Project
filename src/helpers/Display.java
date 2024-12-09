package helpers;

import helpers.displays.AdvisorDisplay;
import helpers.displays.MessageDisplay;

import main.*;
import users.DataAccess;
import users.Instructor;
import users.Registrar;
import users.Student;
import helpers.displays.DirectoryDisplay;




public class Display {
    public static void displayStudentMenu() {
        String id = Utils.getInput("Enter Student ID to login. Please visit the Registrar menu to create an account: ");
        Student student = DataAccess.findStudentById(id);

        if (student == null) {
            displayMessage("Student not found.");
            return;
        }

        while (true) {
            displayMessage("\nStudent Menu:");
            displayMessage("1 Course Management");
            displayMessage("2 Academic Records");
            displayMessage("3 Financial Services");
            displayMessage("4 Student Services");
            displayMessage("5 Library Portal");
            displayMessage("6 Logout");
            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    displayCourseManagementMenu(student);
                    break;
                case "2":
                    displayAcademicRecordsMenu(student);
                    break;
                case "3":
                    displayFinancialServicesMenu(student);
                    break;
                case "4":
                    displayStudentServicesMenu(student);
                    break;
                case "5":
                    displayLibraryPortal(student);
                    break;
                case "6":
                    return;
                default:
                    displayMessage("Invalid option");
            }
        }
    }

    private static void displayAcademicRecordsMenu(Student student) {
        while (true) {
            displayMessage("\nAcademic Records:");
            displayMessage("1 View or Change Major");
            displayMessage("2 View Grades");
            displayMessage("3 View Academic Progress");
            displayMessage("4 Apply for Graduation");
            displayMessage("5 Drop Out");
            displayMessage("6 Back to Main Menu");
            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    student.changeMajorDisplay();
                    break;
                case "2":
                    student.viewGrades();
                    break;
                case "3":
                    student.trackAcademicProgress();
                    break;
                case "4":
                    student.applyForGraduation();
                    break;
                case "5":
                    student.dropOut();
                    break;
                case "6":
                    return; // Back to main menu
                default:
                    displayMessage("Invalid option");
            }
        }
    }

    private static void displayLibraryPortal(Student student) {
        while (true) {
            displayMessage("\nLibrary Menu");
            displayMessage("1. Reserve a study room");
            displayMessage("5. Back to main menu\n");

            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    student.reserveStudyRoom();
                    break;
                case "5":
                    return;
                default:
                    displayMessage("Invalid option. Please try again.");
                    break;
            }
        }
    }


    private static void displayCourseManagementMenu(Student student) {
        while (true) {
            displayMessage("\nCourse Management:");
            displayMessage("1 View My Courses");
            displayMessage("2 Enroll in Course");
            displayMessage("3 Submit Assignment");
            displayMessage("4 Drop a Class");
            displayMessage("5 Back to Main Menu");
            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    student.viewCourses();
                    break;
                case "2":
                    Course.displayAllCourses();
                    student.enrollInCourse();
                    break;
                case "3":
                    student.submitAssignment();
                    break;
                case "4":
                    Enrollment.dropCourse(student.getId());
                    break;
                case "5":
                    return;
                default:
                    displayMessage("Invalid option");
            }
        }
    }

    private static void displayFinancialServicesMenu(Student student) {
        while (true) {
            displayMessage("\nFinancial Services:");
            displayMessage("1 View University Bill");
            displayMessage("2 Apply for Student Housing");
            displayMessage("3 Pay for Student Housing");
            displayMessage("4 Register for Meal Plan");
            displayMessage("5 Back to Main Menu");
            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    student.viewUniversityBillingOptions();
                    break;
                case "2":
                    StudentHousing sh = new StudentHousing(student.getId());
                    sh.apply();
                    break;
                case "3":
                	student.payForHousing();
                    break;
                case "4":
                    MealPlan.chooseMealPlan(student.id);
                    break;
                case "5":
                    return;
                default:
                    displayMessage("Invalid option");
            }
        }
    }

    private static void displayStudentServicesMenu(Student student) {
        while (true) {
            displayMessage("\nStudent Services:");
            displayMessage("1 View Extracurricular Activities");
            displayMessage("2 View Messages");
            displayMessage("3 Directory Menu");
            displayMessage("4 Schedule Meeting with Advisor");
            displayMessage("5 Provide Professor Feedback");
            displayMessage("6 View University Events");
            displayMessage("7 Back to Main Menu");
            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    student.displayStudentExtracurricularMenu();
                    break;
                case "2":
                    MessageDisplay messageDisplay = new MessageDisplay(student.getId(), student.getName());
                    messageDisplay.displayMessageMenu();
                    break;
                case "3":
                    DirectoryDisplay directoryDisplay = new DirectoryDisplay();
                    directoryDisplay.displayDirectoryMenu();
                    break;
                case "4":
                    AdvisorDisplay advisorDisplay = new AdvisorDisplay(student.getAdvisor());
                    advisorDisplay.displayAdvisorMenuForStudents(student.getId());
                    break;
                case "5":
                    student.submitFeedback(); // Call method for submitting feedback
                    break;
                case "6":
                    student.displayEventMenu();
                    break;
                case "7":
                    return;
                default:
                    displayMessage("Invalid option");
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
            displayMessage("3 View Messages");
            displayMessage("4 Create Course");  // New option to create a course
            displayMessage("5 View Feedback");
            displayMessage("6 Logout");
            displayMessage("7 Directory");
            String choice = Utils.getInput("Select an option: \n");

            switch (choice) {
                case "1":
                    instructor.viewCourses();
                    break;
                case "2":
                    instructor.gradeAssignments();
                    break;
                case "3":
                    MessageDisplay messageDisplay = new MessageDisplay(instructor.getId(), instructor.getName());
                    messageDisplay.displayMessageMenu();
                    break;
                case "4":  // New case to create a course
                    instructor.createCourse();
                    break;
                case "5":
                    instructor.viewFeedback();
                    break;
                case "6":
                    return;
                case "7":
                    DirectoryDisplay directoryDisplay = new DirectoryDisplay();
                    directoryDisplay.displayDirectoryMenu();
                    break;
                default:
                    displayMessage("Invalid option.");
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