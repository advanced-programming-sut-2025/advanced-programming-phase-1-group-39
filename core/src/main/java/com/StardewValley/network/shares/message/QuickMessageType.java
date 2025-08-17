package com.StardewValley.network.shares.message;

import java.io.Serializable;

public enum QuickMessageType implements Serializable {
    ON_MY_WAY("On my way!"),
    NEED_HELP("I need help!"),
    WAIT_FOR_ME("Wait for me."),
    THANK_YOU("Thank you!"),
    YES("Yes"),
    NO("No");

    private final String messageText;

    QuickMessageType(String text) {
        this.messageText = text;
    }

    public String getMessageText() {
        return messageText;
    }
}