package com.StardewValley.network.shares.dtos;

import com.StardewValley.network.shares.message.QuickMessageType;
import java.io.Serializable;

public class ShowQuickMessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String senderUsername;
    private final QuickMessageType messageType;

    public ShowQuickMessageDTO(String senderUsername, QuickMessageType messageType) {
        this.senderUsername = senderUsername;
        this.messageType = messageType;
    }

    public String getSenderUsername() { return senderUsername; }
    public QuickMessageType getMessageType() { return messageType; }
}