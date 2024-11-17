package users;

import java.util.ArrayList;
import java.util.Stack;

import java.util.List;

public class Inbox  {

    public static class Message {
        private final String messageId;
        private final String senderId;
        private String senderName;
        private final String subject;
        private final String message;
        private boolean isRead;

        public Message(String messageId, String senderId, String senderName, String subject, String message) {
            this.messageId = messageId;
            this.senderId = senderId;
            this.senderName = senderName;
            this.subject = subject;
            this.message = message;
            this.isRead = false;
        }

        public String getMessageId() {
            return messageId;
        }

        public String getSenderId() {
            return senderId;
        }

        public String getSenderName() {
            return senderName;
        }

        public String getSubject() {
            return subject;
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

        public String[] toStringArray(){
            return new String[]{messageId, senderId, senderName, subject, message};
        }
    }

    private final String ownerId;
    private String ownerName;
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
        this.messages.push(message);
        this.size++;
        if (!message.isRead()) {
            this.unreadCount++;
        }
    }

    public void setSize(int i) {
        this.size = i;
    }

    public void setUnreadCount(int i) {
        this.unreadCount = i;
    }

    public void setMessages(Stack<Message> messages) {
        this.messages = messages;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public List<String[]> messagesToStringArray(){
        List<String[]> messageList = new ArrayList<>();
        for (int i = this.messages.size() - 1; i >= 0; i--) {
            messageList.add(messages.get(i).toStringArray());
        }
        return messageList;
    }
}