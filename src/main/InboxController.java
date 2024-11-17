package main;

import Interfaces.InboxInterface;
import Interfaces.RegistrarInterface;
import helpers.FileUtils;
import users.Inbox;
import java.util.Stack;

import java.util.List;

public class InboxController implements InboxInterface {
    private Inbox inbox;
    private String ownerID;
    private String ownerName;
    private static final String[] INBOX_DETAILS_HEADERS = {
            "OwnerID::OwnerName::size::unreadCount##"
    };
    private static final String[] MESSAGE_HEADERS = {
            "messageId::senderId::senderName::subject::message##"
    };

    public InboxController(String ownerID, String ownerName) {
        this.ownerID = ownerID;
        this.ownerName = ownerName;
        this.inbox = findInbox(ownerID);
        if (this.inbox == null) {
            this.inbox = new Inbox(ownerID);
        }
    }

    public void addMessagesToInbox() {
        List<String[]> messages = FileUtils.readStructuredData("inbox/inboxes", ownerID + ".txt");
        for (String[] stringMessage : messages) {
            Inbox.Message message = new Inbox.Message(stringMessage[0], stringMessage[1], stringMessage[2], stringMessage[3], stringMessage[4]);
            this.inbox.addMessage(message);
        }
    }



    @Override
    public Stack<Inbox.Message> getAllMessages() {
        return inbox.getMessages();
    }

    @Override
    public void viewMessage(Inbox.Message message) {
        message.setRead(true);
        System.out.println("Sender: " + message.getSenderName());
        System.out.println("Subject: " + message.getSubject());
        System.out.println("Message: " + message.getMessage());
    }


    @Override
    public boolean  deleteMessage(String messageId) {
        List<String[]> messages = FileUtils.readStructuredData("inbox/inboxes", ownerID + ".txt");
        for (String[] stringMessage : messages) {
            if (stringMessage[0].equals(messageId)) {
                messages.remove(stringMessage);
                FileUtils.writeStructuredData("inbox/inboxes", ownerID + ".txt", MESSAGE_HEADERS, messages);
                System.out.println("Message successfully deleted");
                return true;
            }
        }
        System.out.println("Message not found");
        return false;
    }

    @Override
    public boolean sendMessage(String recipientID, String subject, String body) {
        Inbox recipiantInbox = findInbox(recipientID);
        if (recipiantInbox == null) {
            System.out.println("Recipient not found");
            return false;
        }
        String messageID = RegistrarInterface.generateStudentId();
        boolean duplicateIDs = true;
        List<String[]> messages = FileUtils.readStructuredData("inbox/inboxes", this.ownerID + ".txt");
        while (duplicateIDs && messages.size() > 0) {
            for (String[] stringMessage : messages) {
                if (stringMessage[0].equals(messageID)) {
                    messageID = RegistrarInterface.generateStudentId();
                    break;
                }
                duplicateIDs = false;
            }
        }

        Inbox.Message message = new Inbox.Message(messageID, recipientID, this.ownerName, subject, body);
        recipiantInbox.addMessage(message);
        FileUtils.writeStructuredData("inbox/inboxes", recipientID + ".txt", MESSAGE_HEADERS, inbox.messagesToStringArray());
        System.out.println("Message sent");
        return true;
    }

    @Override
    public void viewDrafts() {

    }

    @Override
    public void editDraft(String messageId) {

    }

    @Override
    public boolean sendDraft(String messageId) {
        return false;
    }

    @Override
    public boolean saveDraft(String body) {
        return false;
    }

    @Override
    public Inbox findInbox(String ownerId) {
        List<String[]>inboxes = FileUtils.readStructuredData("inbox", "inboxList.txt");
        Inbox inbox = null;
        for (String[] inboxString : inboxes) {
            if (inboxString[0].equals(ownerID)) {
                inbox = new Inbox(ownerID);
                inbox.setSize(Integer.parseInt(inboxString[2]));
                inbox.setUnreadCount(Integer.parseInt(inboxString[3]));
                addMessagesToInbox();
                break;
            }
        }
        return inbox;

//        List<String[]> inboxes = FileUtils.readStructuredData("inbox", "inboxList.txt");
//
//        for (String[] inbox : inboxes) {
//            if (inbox[0].equals(ownerId)) {
//
//            }
//        }
//        return null;
    }
}
