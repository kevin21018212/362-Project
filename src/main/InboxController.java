package main;

import Interfaces.InboxInterface;
import helpers.FileUtils;
import users.Inbox;
import java.util.Stack;

import java.util.List;

public class InboxController implements InboxInterface {
    private Inbox inbox;
    private String ownerID;
    private static final String[] INBOX_DETAILS_HEADERS = {
            "OwnerID::InboxID::size::unreadCount##"
    };
    private static final String[] MESSAGE_HEADERS = {
            "messageId::senderId::senderName::subject::message##"
    };

    public InboxController(String ownerID) {
        this.ownerID = ownerID;
        this.inbox = findInbox(ownerID);
    }

    @Override
    public void setInbox() {


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
    public boolean sendMessage(String recipient, String subject, String body) {
        String inboxID = findInbox(recipient);


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
        if (inbox == null) {
            inbox = new Inbox(this.ownerID);
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
