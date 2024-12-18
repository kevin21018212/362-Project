package main;

import Interfaces.InboxInterface;
import Interfaces.RegistrarInterface;
import helpers.FileUtils;
import helpers.Utils;
import users.Inbox;

import java.util.ArrayList;
import java.util.Stack;

import java.util.List;

/**
 * Controller class for managing user inboxes and message handling.
 * Implements InboxInterface for standardized inbox operations.
 */
public class InboxController implements InboxInterface {
    private Inbox inbox;
    private String ownerID;
    private String ownerName;
    private static final String[] INBOX_DETAILS_HEADERS = {
            "OwnerID::OwnerName::size::unreadCount##"
    };
    private static final String[] MESSAGE_HEADERS = {
            "messageId::senderId::senderName::subject::message##"
    };

    /**
     * Constructs a new InboxController for a specific user.
     * @param ownerID The ID of the inbox owner
     * @param ownerName The name of the inbox owner
     */
    public InboxController(String ownerID, String ownerName) {
        this.ownerID = ownerID;
        this.ownerName = ownerName;
        this.inbox = findInbox(ownerID);
        if (this.inbox == null) {
            this.inbox = new Inbox(ownerID);
        }
    }

    /**
     * Adds messages from file storage to an inbox.
     * @param inbox The inbox to populate with messages
     */
    public void addMessagesToInbox(Inbox inbox) {
        List<String[]> messages = FileUtils.readStructuredData("inbox/inboxes", inbox.getOwnerId() + ".txt");
//        for (String[] stringMessage : messages) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            String[] stringMessage = messages.get(i);
            Inbox.Message message = new Inbox.Message(stringMessage[0], stringMessage[1], stringMessage[2], stringMessage[3], stringMessage[4]);
            inbox.addMessage(message);
        }
    }

    /**
     * Retrieves all messages in the inbox.
     * @return Stack of messages in the inbox
     */
    @Override
    public Stack<Inbox.Message> getAllMessages() {
        return inbox.getMessages();
    }

    /**
     * Displays a message and marks it as read.
     * @param message The message to view
     */
    @Override
    public void viewMessage(Inbox.Message message) {
        message.setRead(true);
        System.out.println("Sender: " + message.getSenderName());
        System.out.println("Subject: " + message.getSubject());
        System.out.println("Message: " + message.getMessage());
    }

    /**
     * Deletes a message from the inbox.
     * @param messageId ID of the message to delete
     * @return true if deletion was successful, false otherwise
     */
    @Override
    public boolean  deleteMessage(String messageId) {
        List<String[]> messages = FileUtils.readStructuredData("inbox/inboxes", ownerID + ".txt");
        for (String[] stringMessage : messages) {
            if (stringMessage[0].equals(messageId)) {
                messages.remove(stringMessage);
                FileUtils.writeStructuredData("inbox/inboxes", ownerID + ".txt", MESSAGE_HEADERS, messages);
                System.out.println("Message successfully deleted");
                return true;
            }
        }
        System.out.println("Message not found");
        return false;
    }

    /**
     * Sends a message to a recipient.
     * @param recipientID ID of the recipient
     * @param subject Message subject
     * @param body Message content
     * @return true if message was sent successfully, false otherwise
     */
    @Override
    public boolean sendMessage(String recipientID, String subject, String body) {
        Inbox recipiantInbox = findInbox(recipientID);
        if (recipiantInbox == null) {
            System.out.println("Recipient not found");
            return false;
        }

        String messageID = genMessageID(recipientID);

        Inbox.Message message = new Inbox.Message(messageID, recipientID, this.ownerName, subject, body);
        recipiantInbox.addMessage(message);
        FileUtils.writeStructuredData("inbox/inboxes", recipientID + ".txt", MESSAGE_HEADERS, recipiantInbox.messagesToStringArray());
        System.out.println("Message sent");
        return true;
    }

    /**
     * Displays all drafts and handles draft operations.
     */
    @Override
    public void viewDrafts() {
        List<String[]> drafts = FileUtils.readStructuredData("inbox/drafts", ownerID + ".txt");
        if (drafts.isEmpty()) {
            System.out.println("No drafts found");
            return;
        }

        System.out.println("\nDrafts:");
        int i = 1;
        for (String[] draft : drafts) {
            System.out.println(i + ": Draft ID: " + draft[0]);
            System.out.println("Recipient: " + draft[1]);
            System.out.println("Subject: " + draft[2]);
            System.out.println("Message: " + draft[3]);
            System.out.println("---------------");
            i ++;
        }

        System.out.println("Select a draft or press 0 to exit");
        String input = Utils.getInput("Select draft: ");

        int draftIndex = 0;

        if (input.equals("0")) return;
        try {
            draftIndex = Integer.parseInt(input)-1;
            if (draftIndex < 0 || draftIndex > drafts.size()) {
                System.out.println("Invalid draft ID");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }
        System.out.println("1: Edit draft\n2: Send draft\n3: Delete draft\n0: Cancel");
        String draftOption = Utils.getInput("Select an option: ");
        switch (draftOption) {
            case "0":
                return;
            case "1":
                editDraft(drafts.get(draftIndex)[0]);
                break;
            case "2":
                sendDraft(drafts.get(draftIndex)[0]);
                break;
            case "3":
                deleteDraft(drafts.get(draftIndex)[0]);
                break;
            default:
                System.out.println("Invalid draft Option");
        }

    }

    /**
     * Edits an existing draft message.
     * @param messageId ID of the draft to edit
     */
    @Override
    public void editDraft(String messageId) {
        List<String[]> drafts = FileUtils.readStructuredData("inbox/drafts", ownerID + ".txt");
        List<String[]> updatedDrafts = new ArrayList<>();
        boolean draftFound = false;

        for (String[] draft : drafts) {
            if (draft[0].equals(messageId)) {
                draftFound = true;
                System.out.println("Current draft details:");
                System.out.println("Recipient: " + draft[1]);
                System.out.println("Subject: " + draft[2]);
                System.out.println("Content: " + draft[3]);

                // Get updated information
                String newRecipient = Utils.getInput("Enter new recipient (press Enter to keep current): ");
                String newSubject = Utils.getInput("Enter new subject (press Enter to keep current): ");
                String newContent = Utils.getInput("Enter new content (press Enter to keep current): ");

                // Update only if new value provided
                String[] updatedDraft = new String[4];
                updatedDraft[0] = messageId;
                updatedDraft[1] = newRecipient.isEmpty() ? draft[1] : newRecipient;
                updatedDraft[2] = newSubject.isEmpty() ? draft[2] : newSubject;
                updatedDraft[3] = newContent.isEmpty() ? draft[3] : newContent;

                updatedDrafts.add(updatedDraft);
            } else {
                updatedDrafts.add(draft);
            }
        }

        if (!draftFound) {
            System.out.println("Draft not found");
            return;
        }

        // Save updated drafts
        FileUtils.writeStructuredData("inbox/drafts", ownerID + ".txt",
                new String[]{"messageId::recipientId::subject::content##"},
                updatedDrafts);
        System.out.println("Draft updated successfully");
    }

    /**
     * Saves a new draft message.
     * @param recipientId ID of the intended recipient
     * @param subject Draft subject
     * @param body Draft content
     * @return true if draft was saved successfully, false otherwise
     */
    @Override
    public boolean saveDraft(String recipientId, String subject, String body) {

        List<String[]> drafts = FileUtils.readStructuredData("inbox/drafts", ownerID + ".txt");

        // Create new draft
        String[] newDraft = new String[]{
                genMessageID(recipientId),
                recipientId,
                subject,
                body
        };
        drafts.add(newDraft);

        // Save to drafts folder
        try {
            FileUtils.writeStructuredData("inbox/drafts", ownerID + ".txt",
                    MESSAGE_HEADERS,
                    drafts);
            System.out.println("Draft saved successfully");
            return true;
        } catch (Exception e) {
            System.out.println("Error saving draft: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sends an existing draft message.
     * @param messageId ID of the draft to send
     * @return true if draft was sent successfully, false otherwise
     */
    @Override
    public boolean sendDraft(String messageId) {
        List<String[]> drafts = FileUtils.readStructuredData("inbox/drafts", ownerID + ".txt");
        List<String[]> remainingDrafts = new ArrayList<>();
        boolean sent = false;

        for (String[] draft : drafts) {
            if (draft[0].equals(messageId)) {
                // Send the draft as a message
                if (sendMessage(draft[1], draft[2], draft[3])) {
                    sent = true;
                    sendMessage(draft[1], draft[2], draft[3]);
                    continue; // Don't add to remaining drafts
                }
            }
            remainingDrafts.add(draft);
        }

        if (sent) {
            // Update drafts file without the sent draft
            FileUtils.writeStructuredData("inbox/drafts", ownerID + ".txt",
                    new String[]{"messageId::recipientId::subject::content##"},
                    remainingDrafts);
            System.out.println("Draft sent successfully");
            return true;
        } else {
            System.out.println("Failed to send draft");
            return false;
        }
    }

    /**
     * Deletes a draft message.
     * @param messageId ID of the draft to delete
     * @return true if draft was deleted successfully, false otherwise
     */
    @Override
    public boolean deleteDraft(String messageId) {
        List<String[]> drafts = FileUtils.readStructuredData("inbox/drafts", ownerID + ".txt");
        List<String[]> remainingDrafts = new ArrayList<>();
        boolean draftFound = false;

        // Filter out the draft to be deleted
        for (String[] draft : drafts) {
            if (draft[0].equals(messageId)) {
                draftFound = true;
                continue;
            }
            remainingDrafts.add(draft);
        }

        if (!draftFound) {
            System.out.println("Draft not found");
            return false;
        }

        // Update drafts file without the deleted draft
        try {
            FileUtils.writeStructuredData("inbox/drafts", ownerID + ".txt",
                    new String[]{"messageId::recipientId::subject::content##"},
                    remainingDrafts);
            System.out.println("Draft deleted successfully");
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting draft: " + e.getMessage());
            return false;
        }
    }

    /**
     * Finds an inbox by owner ID.
     * @param ownerID ID of the inbox owner
     * @return Inbox object if found, null otherwise
     */
    @Override
    public Inbox findInbox(String ownerID) {
        List<String[]>inboxes = FileUtils.readStructuredData("inbox", "inboxList.txt");
        Inbox inbox = null;
        for (String[] inboxString : inboxes) {
            if (inboxString[0].equals(ownerID)) {
                inbox = new Inbox(ownerID);
                inbox.setSize(Integer.parseInt(inboxString[2]));
                inbox.setUnreadCount(Integer.parseInt(inboxString[3]));
                addMessagesToInbox(inbox);
                break;
            }
        }
        return inbox;
    }

    /**
     * Generates a unique message ID for a new message.
     * @param recipientID ID of the message recipient
     * @return Generated unique message ID
     */
    @Override
    public String genMessageID(String recipientID) {
        String messageID = RegistrarInterface.generateStudentId();
        boolean duplicateIDs = true;
        List<String[]> messages = FileUtils.readStructuredData("inbox/inboxes", recipientID + ".txt");
        while (duplicateIDs && messages.size() > 0) {
            for (String[] stringMessage : messages) {
                if (stringMessage[0].equals(messageID)) {
                    messageID = RegistrarInterface.generateStudentId();
                    break;
                }
                duplicateIDs = false;
            }
        }
        return messageID;
    }
}