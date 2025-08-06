package com.StardewValley.network.shares.dtos;

import com.StardewValley.network.shares.message.ReactionType;

import java.io.Serializable;

public class ShowReactionDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String reactingPlayerUsername;
    private final ReactionType reactionType; // به جای String

    public ShowReactionDTO(String reactingPlayerUsername, ReactionType reactionType) {
        this.reactingPlayerUsername = reactingPlayerUsername;
        this.reactionType = reactionType;
    }

    public String getReactingPlayerUsername() { return reactingPlayerUsername; }
    public ReactionType getReactionType() { return reactionType; }
}