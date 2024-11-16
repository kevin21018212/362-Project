package Interfaces;

import users.Inbox;
import java.util.List;

public interface InboxInterface {
    List<Inbox.Message> getMessages();
    Inbox.Message viewMessage(String messageId);
    boolean markAsRead(String messageId);
    void deleteMessage(String messageId);
}

