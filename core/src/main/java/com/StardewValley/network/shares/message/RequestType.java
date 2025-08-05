package com.StardewValley.network.shares.message;

public enum RequestType {
    CREATE_LOBBY,       // درخواست ایجاد لابی از کلاینت به سرور
    JOIN_LOBBY,         // <<-- این خط اضافه شده است -- درخواست پیوستن به لابی
    GET_LOBBY_LIST,     // درخواست لیست لابی‌ها از کلاینت به سرور
    UPDATE_LOBBY_LIST,
    LOBBY_CHAT_MESSAGE// ارسال لیست به‌روز شده لابی‌ها از سرور به کلاینت‌ها
}