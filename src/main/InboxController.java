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

    public InboxController(String owenerID) {
        this.ownerID = ownerID;

    }

    @Override
    public void setInbox() {
        List<String[]>inboxes = FileUtils.readStructuredData("inbox", "inboxList.txt");
        Inbox inbox = null;
        for (String[] inboxString : inboxes) {
            if (inboxString[0].equals(ownerID)) {
                this.inbox = new Inbox(ownerID);
                this.inbox.setSize(Integer.parseInt(inboxString[2]));
                this.inbox.setUnreadCount(Integer.parseInt(inboxString[3]));
                addMessagesToInbox();
                break;
            }
        }
        if (inbox == null) {
            inbox = new Inbox(ownerID);
        }
    }

    public void addMessagesToInbox() {
        List<String[]> messages = FileUtils.readStructuredData("inbox/inboxes", ownerID + ".txt");
        for (String[] stringMessage : messages) {
            Inbox.Message message = new Inbox.Message(stringMessage[0], stringMessage[1], stringMessage[2], stringMessage[3]);
            this.inbox.addMessage(message);
        }
    }



    @Override
    public Stack<Inbox.Message> getAllMessages() {
        return inbox.getMessages();
    }

    @Override
    public Inbox.Message viewMessage(String messageId) {
        return null;
    }

    @Override
    public boolean markAsRead(String messageId) {
        return false;
    }

    @Override
    public void deleteMessage(String messageId) {

    }
}
