package com.StardewValley.network.shares.message;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L; // برای سریال‌سازی

    private final RequestType type;
    private final Object payload; // داده‌های همراه درخواست

    public Request(RequestType type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public RequestType getType() {
        return type;
    }

    public Object getPayload() {
        return payload;
    }
}