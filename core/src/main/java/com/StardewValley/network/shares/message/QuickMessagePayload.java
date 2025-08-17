package com.StardewValley.network.shares.message;

import java.io.Serializable;

public class QuickMessagePayload implements Serializable {
    private static final long serialVersionUID = 1L;
    private final QuickMessageType messageType;

    public QuickMessagePayload(QuickMessageType messageType) {
        this.messageType = messageType;
    }

    public QuickMessageType getMessageType() {
        return messageType;
    }
}