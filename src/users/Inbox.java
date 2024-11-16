package users;

import Interfaces.InboxInterface;

import java.util.List;

public class Inbox implements InboxInterface {
    public class Message{

    }

    @Override
    public List<Message> getMessages() {
        return null;
    }

    @Override
    public Message viewMessage(String messageId) {
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
