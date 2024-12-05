package helpers.displays;

import helpers.Utils;
import main.AdvisorController;
import users.Advisor;

import java.util.ArrayList;

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
                    cancelMeeting();
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
            System.out.println("0: Exit");

            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "0":
                    return;
                case "1":
                    advisorController.printSchedule();
                    break;
                case "2":
                    addMeeting(null);
                    break;
                case "3":
                    cancelMeeting();
                    break;

                default:
                    System.out.println("Invalid option");
            }
        }
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

    private void cancelMeeting() {
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

        String studentId = Utils.getInput("Enter student ID: ");
        advisorController.cancelMeeting(day, time, studentId);
    }

    private void addStudent() { //TODO: Implement
        System.out.println("Enter Student IDs to Add: ");
        ArrayList<String> students = new ArrayList<>();
        String studentId = Utils.getInput("Enter student ID or 0 to stop: ");
        while (!studentId.equals("0")) {
            students.add(studentId);
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
        //TODO
    }

    private void removeRegistrationHold() {
        //TODO
    }
}