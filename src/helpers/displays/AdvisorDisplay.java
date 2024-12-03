package helpers.displays;

import helpers.Utils;
import main.AdvisorController;
import users.Advisor;

public class AdvisorDisplay {
    AdvisorController advisorController;

    public  AdvisorDisplay(String id) {
        this.advisorController = new AdvisorController(id);
    }

    public void displayAdvisorMenuForStudents() {
        while (true) {
            System.out.println("\nAdvisor Menu:");
            System.out.println("1: View Schedule");
            System.out.println("2: Add Meeting");
            System.out.println("3: Cancel Meeting");
            System.out.println("4: Exit");

            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    advisorController.printSchedule();
                    break;
                case "2":
                    addMeeting();
                    break;
                case "3":
                    cancelMeeting();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void addMeeting() {
        int day = Integer.parseInt(Utils.getInput("Enter day: "));
        int time = Integer.parseInt(Utils.getInput("Enter time: "));
        String studentId = Utils.getInput("Enter student ID: ");
        advisorController.addMeeting(day, time, studentId);
    }

    private void cancelMeeting() {
        int day = Integer.parseInt(Utils.getInput("Enter day: "));
        int time = Integer.parseInt(Utils.getInput("Enter time: "));
        String studentId = Utils.getInput("Enter student ID: ");
        advisorController.cancelMeeting(day, time, studentId);
    }
}
