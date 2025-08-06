package com.StardewValley.network.shares.message;

public enum RequestType {
    // first connect to server
    SEND_IDENTITY,

    //Lobby and Online Users
    UPDATE_LOBBY_LIST,
    UPDATE_ONLINE_USERS,

    CREATE_LOBBY,
    JOIN_LOBBY,
    GET_LOBBY_LIST,
    LOBBY_CHAT_MESSAGE,

    //Start Game
    START_GAME,
    GAME_STARTED,
    GAME_START_FAILED,

    //InGame
    PlAYER_MOVE,
    UPDATE_GAME_STATE,
    SHOR_ERROR_MESSAGE,
    BUILD_GREENHOUSE,
}