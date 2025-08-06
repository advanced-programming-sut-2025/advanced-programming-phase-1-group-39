package com.StardewValley.network.shares.dtos;

import java.io.Serializable;

public class ShowReactionDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String reactingPlayerUsername;
    private final String reactionContent;

    public ShowReactionDTO(String reactingPlayerUsername, String reactionContent) {
        this.reactingPlayerUsername = reactingPlayerUsername;
        this.reactionContent = reactionContent;
    }

    public String getReactingPlayerUsername() {
        return reactingPlayerUsername;
    }

    public String getReactionContent() {
        return reactionContent;
    }
}