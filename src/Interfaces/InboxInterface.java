package Interfaces;

import users.Inbox;

import java.util.Stack;

public interface InboxInterface {
    void setInbox();
    Stack<Inbox.Message> getAllMessages();
    Inbox.Message viewMessage(String messageId);
    boolean markAsRead(String messageId);
    void deleteMessage(String messageId);
}

