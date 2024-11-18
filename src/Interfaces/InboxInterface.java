package Interfaces;

import users.Inbox;

import java.util.Stack;

public interface InboxInterface {
    Stack<Inbox.Message> getAllMessages();
    void viewMessage(Inbox.Message message);
    boolean deleteMessage(String messageId);
    boolean sendMessage(String recipient, String subject, String body);
    void viewDrafts();
    void editDraft(String messageId);
    boolean sendDraft(String messageId);
    boolean saveDraft(String recipientId, String subject, String body);

    boolean deleteDraft(String messageId);

    Inbox findInbox(String ownerId);
    String genMessageID(String recipientID);
}

