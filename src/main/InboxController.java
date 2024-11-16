package main;

import Interfaces.InboxInterface;
import users.Inbox;

import java.util.List;

public class InboxController implements InboxInterface {

    @Override
    public List<Inbox.Message> getMessages() {
        return List.of();
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
