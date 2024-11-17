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
                    viewDrafts();
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

        int i = 1;
        while (!messageCopy.isEmpty()) {
            Inbox.Message message = messageCopy.pop();
            System.out.println(i + ". Subject: " + message.getSubject());
            System.out.println("   Read: " + message.isRead());
            i++;
        }

        String input = Utils.getInput("Enter message ID to view (or 0 to exit): ");
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
                inboxController.sendMessage(recipient, subject, body);
                System.out.println("Message sent successfully");
                break;
            case "2":
//                inboxController.saveDraft(recipient, subject, body);
                System.out.println("Draft saved successfully");
                break;
            case "3":
                System.out.println("Message cancelled");
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    private void viewDrafts() {
//        Stack<Inbox.Message> drafts = inboxController.getDrafts();
//        if (drafts.isEmpty()) {
//            System.out.println("No drafts found");
//            return;
//        }
//
//        int i = 1;
//        for (Inbox.Message draft : drafts) {
//            System.out.println(i + ". Subject: " + draft.getSubject());
//            i++;
//        }
//
//        String input = Utils.getInput("Enter draft ID to edit (or 0 to exit): ");
//        if (input.equals("0")) return;
//
//        try {
//            int draftIndex = Integer.parseInt(input);
//            if (draftIndex > 0 && draftIndex <= drafts.size()) {
//                editDraft(drafts.get(draftIndex - 1));
//            } else {
//                System.out.println("Invalid draft ID");
//            }
//        } catch (NumberFormatException e) {
//            System.out.println("Invalid input");
//        }
    }

    private void editDraft(Inbox.Message draft) {
//        System.out.println("\nCurrent draft:");
//        System.out.println("Recipient: " + draft.getRecipientId());
//        System.out.println("Subject: " + draft.getSubject());
//        System.out.println("Body: " + draft.getBody());
//
//        String recipient = Utils.getInput("Enter new recipient (or press Enter to keep current): ");
//        String subject = Utils.getInput("Enter new subject (or press Enter to keep current): ");
//        String body = Utils.getInput("Enter new body (or press Enter to keep current): ");
//
//        // Update only if new values provided
//        if (!recipient.isEmpty()) draft.setRecipientId(recipient);
//        if (!subject.isEmpty()) draft.setSubject(subject);
//        if (!body.isEmpty()) draft.setBody(body);
//
//        inboxController.updateDraft(draft);
//        System.out.println("Draft updated successfully");
    }
}