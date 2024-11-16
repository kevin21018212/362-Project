package helpers.displays;

import helpers.Utils;
import main.InboxController;

public class MessageDisplay {
    public static void displayMessageMenu() {
        System.out.println("\nMessage Menu:");
        System.out.println("1: View Messages");
        System.out.println("2: Compose Message");
        System.out.println("4: View Drafts");
        System.out.println("5: Exit");

        String choice = Utils.getInput("Select an option: ");

        switch (choice) {
            case "1":
                // InboxController.viewMessages();
                break;
            case "2":
                // InboxController.composeMessage();
                break;
            case "3":
                // InboxController.viewDrafts();
                break;
            case "4":
                return;
            default:
                System.out.println("Bad Baka");
        }
    }

    public static void displayMessage(String message) {
        System.out.println(message);
    }

    public static void composeMessage() {
        String recipient = Utils.getInput("Enter recipient: ");
        String subject = Utils.getInput("Enter subject: ");
        String body = Utils.getInput("Enter message: ");

        // InboxController.sendMessage(recipient, subject, body);
    }

    public static  void viewDrafts() {
        // InboxController.viewDrafts();
    }

}
