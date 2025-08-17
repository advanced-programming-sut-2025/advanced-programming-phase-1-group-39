package com.StardewValley.network.shares.dtos;

import java.io.Serializable;

public class ChatMessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String senderUsername;
    private final String messageContent;
    private final boolean isPrivate;

    public ChatMessageDTO(String senderUsername, String messageContent, boolean isPrivate) {
        this.senderUsername = senderUsername;
        this.messageContent = messageContent;
        this.isPrivate = isPrivate;
    }

    public String getSenderUsername() { return senderUsername; }
    public String getMessageContent() { return messageContent; }
    public boolean isPrivate() { return isPrivate; }

    @Override
    public String toString() {
        String prefix = isPrivate ? "[Private] " : "[All] ";
        return prefix + senderUsername + ": " + messageContent;
    }
}