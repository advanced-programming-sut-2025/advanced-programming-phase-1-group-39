package com.StardewValley.network.shares.message;


public class PrivateChatMessage extends PublicChatMessage {
    private static final long serialVersionUID = 1L;

    private final String recipientName;

    public PrivateChatMessage(String senderName, String recipientName, String messageContent) {
        super(senderName, messageContent);
        this.recipientName = recipientName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    // می‌توانید toString را برای نمایش بهتر override کنید
    @Override
    public String toString() {
        // [Private to RecipientName] SenderName: Message
        return "[Private to " + recipientName + "] " + getSenderName() + ": " + getMessageContent();
    }
}