package Interfaces;

import users.Inbox;

import java.util.Stack;

public interface InboxInterface {
    void setInbox();
    Stack<Inbox.Message> getAllMessages();
    void viewMessage(Inbox.Message message);
    boolean deleteMessage(String messageId);
}

