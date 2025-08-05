package com.StardewValley.network.shares.message;

public enum RequestType {
    CREATE_LOBBY,       // درخواست ایجاد لابی از کلاینت به سرور
    GET_LOBBY_LIST,     // درخواست لیست لابی‌ها از کلاینت به سرور
    UPDATE_LOBBY_LIST   // ارسال لیست به‌روز شده لابی‌ها از سرور به کلاینت‌ها
    // انواع دیگر درخواست‌ها در آینده اینجا اضافه می‌شوند
}