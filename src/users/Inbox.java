package users;

import java.util.Stack;

import java.util.List;

public class Inbox  {
    public static class Message {
        private final String messageId;
        private final String senderId;
        private final String message;
        private boolean isRead;

        public Message(String messageId, String senderId, String message) {
            this.messageId = messageId;
            this.senderId = senderId;
            this.message = message;
            this.isRead = false;
        }

        public String getMessageId() {
            return messageId;
        }

        public String getSenderId() {
            return senderId;
        }

        public String getMessage() {
            return message;
        }

        public boolean isRead() {
            return isRead;
        }

        public void setRead(boolean read) {
            isRead = read;
        }
    }

    private String ownerId;
    private Stack<Message> messages;
    private int size;
    private int unreadCount;

    public Inbox(String userId) {
        this.ownerId = userId;
        this.messages = new Stack<>();
        this.size = 0;
        this.unreadCount = 0;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Stack<Message> getMessages() {
        return messages;
    }

    public int getSize() {
        return size;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void addMessage(Message message) {
        messages.push(message);
        size++;
        if (!message.isRead()) {
            unreadCount++;
        }
    }
}