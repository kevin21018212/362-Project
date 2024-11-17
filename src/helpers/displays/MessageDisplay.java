package helpers.displays;

import helpers.Utils;
import main.InboxController;
import users.Inbox;

import java.util.Stack;

import static helpers.Utils.getInput;

public class MessageDisplay {
    public static void displayMessageMenu(String userID) {
        InboxController inboxController = new InboxController(userID);

        System.out.println("\nMessage Menu:");
        System.out.println("1: View Messages");
        System.out.println("2: Compose Message");
        System.out.println("4: View Drafts");
        System.out.println("5: Exit");

        String choice = getInput("Select an option: ");

        switch (choice) {
            case "1":
                viewMessages(inboxController);
                break;
            case "2":
                composeMessage();
                break;
            case "3":
                 viewDrafts();
                break;
            case "4":
                return;
            default:
                System.out.println("Bad Baka");
        }
    }

    public static void viewMessages(InboxController inboxController) {
       Stack<Inbox.Message> messages = inboxController.getAllMessages();
        Stack<Inbox.Message> messageCopy = (Stack<Inbox.Message>) messages.clone();
        int i = 1;
        while (!messageCopy.isEmpty()) {
            Inbox.Message message = messages.pop();
            System.out.println(i + "Subject: " + message.getSubject());
            System.out.println("Read: " + message.isRead());
            i += 1;
        }
        int in = Integer.parseInt(getInput("Enter message ID to view: "));
        Inbox.Message message = messages.get(in - 1);
        inboxController.viewMessage(message);
        String choice = getInput("1: Mark as unread\n2: Delete\n3: Exit");
        switch (choice) {
            case "1":
                message.setRead(false);
                break;
            case "2":
                inboxController.deleteMessage(message.getMessageId());
                break;
            case "3":
                return;
            default:
                System.out.println("Bad Baka");
        }

    }



    public static void composeMessage() {
        String recipient = getInput("Enter recipient ID: ");
        String subject = getInput("Enter subject: ");
        String body = getInput("Enter message: ");

         InboxController.sendMessage(recipient, subject, body);
    }

    public static  void viewDrafts() {
        // InboxController.viewDrafts();
    }

}
