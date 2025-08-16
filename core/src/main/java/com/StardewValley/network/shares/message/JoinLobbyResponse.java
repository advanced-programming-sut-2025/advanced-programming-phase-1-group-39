package com.StardewValley.network.shares.message;

import com.StardewValley.models.Result;
import com.StardewValley.network.shares.Lobby;

import java.io.Serializable;

public class JoinLobbyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    public Result joiningResult;
    public Lobby joinedLobby;

    public JoinLobbyResponse(Result joiningResult, Lobby joinedLobby) {
        this.joiningResult = joiningResult;
        this.joinedLobby = joinedLobby;
    }
}
