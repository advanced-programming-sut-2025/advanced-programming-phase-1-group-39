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

    //Start Game
    CHOOSE_MAP,
    START_GAME,
    GAME_STARTED,
    GAME_START_FAILED,

    // chat
    LOBBY_PUBLIC_CHAT_MESSAGE,
    LOBBY_PRIVATE_CHAT_MESSAGE,

    PLAYER_REACTION,
    SHOW_REACTION_ON_PLAYER,

    //InGame
    UPDATE_GAME_STATE,
    PLAYER_DISCONNECTED,
    PLAYER_RECONNECTED,

    PLAYER_MOVE,
    BUILD_GREENHOUSE,

}