package models;

import java.util.Objects;

public record Location(int x, int y) {
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
}
