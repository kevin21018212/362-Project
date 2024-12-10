package helpers.displays;

import helpers.FileUtils;
import helpers.Utils;
import main.AdvisorController;
import users.Advisor;
import users.Student;

import java.util.ArrayList;
import java.util.List;

public class AdvisorDisplay {
    AdvisorController advisorController;

    public  AdvisorDisplay(String id) {
        this.advisorController = new AdvisorController(id);
    }

    public void displayAdvisorMenuForStudents(String studentId) {
        while (true) {
            System.out.println("Advisor Schedule:");
            advisorController.printSchedule();

            System.out.println("\nAdvisor Menu:");
            System.out.println("1: Add Meeting");
            System.out.println("2: Cancel Meeting");
            System.out.println("0: Exit");

            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    addMeeting(studentId);
                    break;
                case "2":
                    cancelMeeting(studentId);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\nAdvisor Menu:");
            System.out.println("1: View Schedule");
            System.out.println("2: Add Meeting");
            System.out.println("3: Cancel Meeting");
            System.out.println("4: Add Student");
            System.out.println("5: Remove Student");
            System.out.println("6: Remove Registration Hold");
            System.out.println("7: View Messages");
            System.out.println("8: Message all students");
            System.out.println("0: Exit");

            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "0":
                    return;
                case "1":
                    advisorController.printSchedule();
                    break;
                case "2":
                    System.out.println("Your students are: "+advisorController.getAllStudents(false));
                    addMeeting(null);
                    break;
                case "3":
                    System.out.println("Your students are: "+advisorController.getAllStudents(false));
                    cancelMeeting(null);
                    break;
                case "4":
                    addStudent();
                    break;
                case "5":
                    removeStudent();
                    break; 
                case "6":
                    System.out.println("Your students are: "+advisorController.getAllStudents(true));
                    removeRegistrationHold();
                    break;
                    case "7":
                        MessageDisplay messageDisplay = new MessageDisplay(advisorController.getAdvisor().getId(), advisorController.getAdvisor().getName());
                        messageDisplay.displayMessageMenu();
                        break;
                    case "8":
                        messageAllStudents();
                        break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void messageAllStudents() {
        String subject = Utils.getInput("Enter subject: ");
        String message = Utils.getInput("Enter message: ");
        advisorController.messageAllStudents(subject, message);
    }

    private void addMeeting(String studentId) {
        advisorController.printSchedule();

        int day = Integer.parseInt(Utils.getInput("Enter day: "));
        day -= 1;
        if (day < Advisor.Days.MONDAY.ordinal() || day > Advisor.Days.FRIDAY.ordinal()) {
            System.out.println("Invalid day");
            return;
        }
        int time = Integer.parseInt(Utils.getInput("Enter time: "));
        time -= 9;
        if (time < 0 || time >= 8) {
            System.out.println("Invalid time");
            return;
        }

        if (studentId == null)
            studentId = Utils.getInput("Enter student ID: ");
        advisorController.addMeeting(day, time, studentId);
    }

    private void cancelMeeting(String studentId) {
        advisorController.printSchedule();

        int day = Integer.parseInt(Utils.getInput("Enter day: "));
        day -= 1;
        if (day < Advisor.Days.MONDAY.ordinal() || day > Advisor.Days.FRIDAY.ordinal()) {
            System.out.println("Invalid day");
            return;
        }
        int time = Integer.parseInt(Utils.getInput("Enter time (e.g. 11:00 as 11): "));
        time -= 9;
        if (time < 0 || time >= 8) {
            System.out.println("Invalid time");
            return;
        }

        if (studentId == null)
            studentId = Utils.getInput("Enter student ID: ");
        advisorController.cancelMeeting(day, time, studentId);
    }

    private void addStudent() { //TODO: TEST
        System.out.println("Enter Student IDs to Add: ");
        ArrayList<String> students = new ArrayList<>();
        List<String[]> allStudents = FileUtils.readStructuredData("", "students.txt");
        String studentId = Utils.getInput("Enter student ID or 0 to stop: ");
        while (!studentId.equals("0")) {
            for (String[] student : allStudents) {
                if (student[0].equals(studentId)) {
                    students.add(studentId);
                    break;
                }
            }
            studentId = Utils.getInput("Enter student ID: ");
        }
        if (students.isEmpty()) {
            System.out.println("No students added.");
            return;
        }
        advisorController.addStudents(advisorController.getAdvisor(), students);
    }

    private void removeStudent() {
        String studentId = Utils.getInput("Enter student ID: ");
        if (advisorController.getAdvisor().getStudents().contains(studentId)) {
            advisorController.removeStudent(studentId);
        } else {
            System.out.println("Student not found.");
        }
    }

    private void removeRegistrationHold() {
        String stuID = Utils.getInput("Enter student ID(0 to cancel): ");
        if (stuID.equals("0")) {
            return;
        }
        List<String[]> allStudents = FileUtils.readStructuredData("", "students.txt");
        Student student = null;
        for (String[] students : allStudents) {
            if (students[0].equals(stuID)) {
                student = new Student(students[0], students[1], students[2], students[3], students[4], students[5], students[6], students[7], students[8]);
                break;
            }
        }
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        if (advisorController.releaseRegistrationHold(student)){
            System.out.println("Hold removed.");
        } else {
            System.out.println("Hold not removed.");
        }
    }
}
