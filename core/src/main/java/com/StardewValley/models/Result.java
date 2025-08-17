package com.StardewValley.models;

import java.io.Serializable;

public record Result(boolean success, String message) implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return message;
    }
}
