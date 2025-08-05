package com.StardewValley.network.shares.message;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String senderName;
    private final String messageContent;

    public ChatMessage(String senderName, String messageContent) {
        this.senderName = senderName;
        this.messageContent = messageContent;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    @Override
    public String toString() {
        return senderName + ": " + messageContent;
    }
}