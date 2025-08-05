package com.StardewValley.network.shares.dtos;

import com.StardewValley.models.Enums.Direction;
import java.io.Serializable;

public class PlayerStateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private float x;
    private float y;
    private Direction direction;

    public PlayerStateDTO(String username, float x, float y, Direction direction) {
        this.username = username;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    // Getter ها
    public String getUsername() { return username; }
    public float getX() { return x; }
    public float getY() { return y; }
    public Direction getDirection() { return direction; }
}