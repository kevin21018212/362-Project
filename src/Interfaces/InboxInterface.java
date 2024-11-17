package Interfaces;

import users.Inbox;

import java.util.Stack;

public interface InboxInterface {
    void setInbox();
    Stack<Inbox.Message> getAllMessages();
    void viewMessage(Inbox.Message message);
    boolean deleteMessage(String messageId);
    boolean sendMessage(String recipient, String subject, String body);
    void viewDrafts();
    void editDraft(String messageId);
    boolean sendDraft(String messageId);
    boolean saveDraft(String body);
    Inbox findInbox(String ownerId);
}

