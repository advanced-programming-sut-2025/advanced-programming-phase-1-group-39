package com.StardewValley.network.shares.message;

import java.io.Serializable;

public class PlayerReactionPayload implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String reactionContent; // می‌تواند متن یا ID یک ایموجی باشد

    public PlayerReactionPayload(String reactionContent) {
        // می‌توانید یک بررسی برای طول متن اینجا اضافه کنید
        if (reactionContent != null && reactionContent.length() > 10) {
            this.reactionContent = reactionContent.substring(0, 10);
        } else {
            this.reactionContent = reactionContent;
        }
    }

    public String getReactionContent() {
        return reactionContent;
    }
}