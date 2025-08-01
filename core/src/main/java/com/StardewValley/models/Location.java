package com.StardewValley.models;

import java.util.Objects;

public class Location {
    float x;
    float y;

    public Location(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Location() {}

    public int x() {
        return (int) x;
    }

    public int y() {
        return (int) y;
    }

    public void addVector(float x, float y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return (x == location.x && y == location.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y +")";
    }
}
