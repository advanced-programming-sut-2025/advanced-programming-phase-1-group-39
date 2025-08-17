package com.StardewValley.network.shares.message;

import java.io.Serializable;

public class ChatMessagePayload implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String messageContent;
    private final String recipientUsername;

    //public
    public ChatMessagePayload(String messageContent) {
        this.messageContent = messageContent;
        this.recipientUsername = null;
    }

    //private
    public ChatMessagePayload(String messageContent, String recipientUsername) {
        this.messageContent = messageContent;
        this.recipientUsername = recipientUsername;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public boolean isPrivate() {
        return recipientUsername != null && !recipientUsername.isEmpty();
    }
}