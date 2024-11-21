package helpers.displays;

import helpers.Utils;
import main.InboxController;
import users.Inbox;
import java.util.Stack;

public class MessageDisplay {
    private final InboxController inboxController;
    private final String userID;
    private final String userName;

    public MessageDisplay(String userID, String userName) {
        this.userID = userID;
        this.userName = userName;
        this.inboxController = new InboxController(userID, userName);
    }

    public void displayMessageMenu() {
        while (true) {
            System.out.println("\nMessage Menu:");
            System.out.println("1: View Messages");
            System.out.println("2: Compose Message");
            System.out.println("3: View Drafts");
            System.out.println("4: Exit");

            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    viewMessages();
                    break;
                case "2":
                    composeMessage();
                    break;
                case "3":
                    inboxController.viewDrafts();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void viewMessages() {
        Stack<Inbox.Message> messages = inboxController.getAllMessages();
        Stack<Inbox.Message> messageCopy = (Stack<Inbox.Message>) messages.clone();

        if (messages.isEmpty()) {
            System.out.println("No messages to display");
            return;
        }

        int i = messageCopy.size();
        while (!messageCopy.isEmpty()) {
            Inbox.Message message = messageCopy.pop();
            System.out.println(i + ". Subject: " + message.getSubject());
            System.out.println("   Read: " + message.isRead());
            i--;
        }

        String input = Utils.getInput("Enter message number to view (or 0 to exit): ");
        if (input.equals("0")) return;

        try {
            int messageIndex = Integer.parseInt(input);
            if (messageIndex > 0 && messageIndex <= messages.size()) {
                Inbox.Message message = messages.get(messageIndex - 1);
                inboxController.viewMessage(message);
                handleMessageOptions(message);
            } else {
                System.out.println("Invalid message ID");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }
    }

    private void handleMessageOptions(Inbox.Message message) {
        while (true) {
            System.out.println("\nMessage Options:");
            System.out.println("1: Mark as unread");
            System.out.println("2: Delete");
            System.out.println("3: Exit");

            String choice = Utils.getInput("Select an option: ");

            switch (choice) {
                case "1":
                    message.setRead(false);
                    return;
                case "2":
                    inboxController.deleteMessage(message.getMessageId());
                    return;
                case "3":
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void composeMessage() {
        String recipient = Utils.getInput("Enter recipient ID: ");
        String subject = Utils.getInput("Enter subject: ");
        String body = Utils.getInput("Enter message: ");

        String choice = Utils.getInput("1: Send\n2: Save as draft\n3: Cancel\nSelect an option: ");

        switch (choice) {
            case "1":
                if (inboxController.sendMessage(recipient, subject, body)) {
                    System.out.println("Message sent successfully");
                } else {
                    System.out.println("Recipient not found");
                }
                break;
            case "2":
                if (inboxController.saveDraft(recipient, subject, body)) {
                    System.out.println("Draft saved successfully");
                } else {
                    System.out.println("Recipient not found");
                }
                break;
            case "3":
                System.out.println("Message cancelled");
                break;
            default:
                System.out.println("Invalid option");
        }
    }
}