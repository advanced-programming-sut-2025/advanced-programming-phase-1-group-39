package com.StardewValley.network.shares.message;

public enum RequestType {
    // first connect to server
    SEND_USERNAME,

    //Lobby and Online Users
    UPDATE_LOBBY_LIST, // server
    UPDATE_ONLINE_USERS,

    REFRESH_LOBBY_LIST, // client
    CREATE_LOBBY,

    JOIN_LOBBY,
    JOIN_LOBBY_RESPONSE, // server


    LOBBY_CHAT_MESSAGE,

    //Start Game
    CHOOSE_MAP,
    START_GAME,
    GAME_STARTED,
    GAME_START_FAILED,

    //InGame
    PlAYER_MOVE,
    UPDATE_GAME_STATE,
    SHOR_ERROR_MESSAGE,
    BUILD_GREENHOUSE,

    PLAYER_DISCONNECTED,
    PLAYER_RECONNECTED,

    PLAYER_REACTION,
    SHOW_REACTION_ON_PLAYER,
}