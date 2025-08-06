package com.StardewValley.network.shares.message;

import com.StardewValley.network.shares.message.ReactionType;
import java.io.Serializable;

public class PlayerReactionPayload implements Serializable {
    private static final long serialVersionUID = 1L;
    private final ReactionType reactionType; // به جای String

    public PlayerReactionPayload(ReactionType reactionType) {
        this.reactionType = reactionType;
    }

    public ReactionType getReactionType() {
        return reactionType;
    }
}