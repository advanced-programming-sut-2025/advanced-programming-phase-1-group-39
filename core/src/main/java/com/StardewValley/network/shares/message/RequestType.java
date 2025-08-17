package com.StardewValley.network.shares.message;

public enum RequestType {
    // first connect to server
    SEND_USER_DATA,

    //Lobby and Online Users
    UPDATE_LOBBY_LIST, // server
    UPDATE_ONLINE_USERS,

    REFRESH_LOBBY_LIST, // client
    CREATE_LOBBY,

    JOIN_PUBLIC_LOBBY,
    JOIN_PRIVATE_LOBBY,
    JOIN_LOBBY_RESPONSE, // server

    //Start Game
    CHOOSE_MAP,
    START_GAME,
    GAME_STARTED,
    GAME_START_FAILED,

    // chat
    LOBBY_PUBLIC_CHAT_MESSAGE,
    LOBBY_PRIVATE_CHAT_MESSAGE,

    SHOW_REACTION_ON_PLAYER,

    //InGame
    PLAYER_DISCONNECTED,
    PLAYER_RECONNECTED,

    /**
     * ارسال قصد حرکت بازیکن.
     * Payload: PlayerMovePayload(float dx, float dy)
     */
    PLAYER_MOVE,

    /**
     * ارسال یک واکنش (ایموجی).
     * Payload: String (نام یا ID واکنش)
     */
    PLAYER_REACTION,

    /**
     * تغییر آیتم فعال در نوار ابزار.
     * Payload: Integer (اندیس اسلات جدید)
     */
    CHANGE_ACTIVE_ITEM,

    /**
     * استفاده از ابزار در دست یا خوردن غذا.
     * Payload: (اختیاری) Direction (جهت استفاده از ابزار)
     */
    USE_TOOL,

    /**
     * تعامل عمومی با یک کاشی (مثل برداشت محصول، باز کردن صندوق).
     * Payload: Location (مختصات کاشی)
     */
    INTERACT_WITH_TILE,

    /**
     * درخواست ساخت یک ساختمان جدید.
     * Payload: BuildRequestPayload(String buildingName, Location location)
     */
    BUILD_BUILDING,

    /**
     * درخواست خرید آیتم از یک فروشگاه.
     * Payload: ShopItemPayload(String itemName, int quantity)
     */
    BUY_ITEM,

    /**
     * درخواست فروش آیتم از طریق صندوق فروش.
     * Payload: ShopItemPayload(String itemName, int quantity)
     */
    SELL_ITEM,


    // === Server to Client Messages ===

    /**
     * ارسال وضعیت کلی و مداوم بازی.
     * Payload: GameStateDTO
     */
    UPDATE_GAME_STATE,

    /**
     * ارسال وضعیت کامل اینونتوری یک بازیکن پس از تغییر.
     * Payload: Inventory (آبجکت کامل اینونتوری بازیکن)
     */
    INVENTORY_UPDATED,

    /**
     * دستور برای نمایش یک انیمیشن در یک مکان خاص.
     * Payload: AnimationPayload(String animationName, Location location)
     */
    SHOW_ANIMATION, YOU_WERE_KICKED, CREATE_LOBBY_SUCCESS, UPDATE_LOBBY_STATE, LEAVE_LOBBY, LEAVE_LOBBY_SUCCESS, // may needed
}