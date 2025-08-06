package com.StardewValley.network.shares.message;

import java.io.Serializable;

public enum ReactionType implements Serializable {
    HAPPY("😄"),
    SAD("😭"),
    LOVE("❤️"),
    ANGRY("😠"),
    QUESTION("❓"),
    IDEA("💡");

    private final String displayText;

    ReactionType(String text) {
        this.displayText = text;
    }

    public String getDisplayText() {
        return displayText;
    }
}