package com.StardewValley.network.shares.message;

import java.io.Serializable;

public class PlayerMovePayload implements Serializable {
    private static final long serialVersionUID = 1L; // برای سریال‌سازی

    private final float dx; // میزان تغییر در محور X
    private final float dy; // میزان تغییر در محور Y

    public PlayerMovePayload(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }
}
