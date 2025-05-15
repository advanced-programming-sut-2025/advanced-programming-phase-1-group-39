package models;

import models.Enums.Season;
import models.Enums.WeatherStatus;
import models.map.Tile;

import java.util.ArrayList;

public class Weather {
    private WeatherStatus status;

    public void thor(Tile tile) {
        if (!status.equals(WeatherStatus.STORM)) return;
        tile.removePlant();
        if (tile.getTree() != null)
            tile.burnTree();
    }

    public void cheatThor(Tile tile) {
        tile.removePlant();
        if (tile.getTree() != null)
            tile.burnTree();
    }

    public void setWeatherRandom(Season season) {
        ArrayList<WeatherStatus> statuses = new ArrayList<>();
        for (WeatherStatus status : WeatherStatus.values()) {
            if (status.canOccurInSeason(season)) {
                statuses.add(status);
            }
        }
        status = WeatherStatus.values()[(int) (Math.random() * statuses.size())];
    }

    public void setStatus(WeatherStatus status) {
        this.status = status;
    }

    public WeatherStatus getStatus() {
        return status;
    }

    public double getFishingFactor() {
        switch (status) {
            case SUNNY -> {
                return 0.5;
            }
            case RAIN -> {
                return 1.2;
            }
            case STORM -> {
                return 1.5;
            }
            default -> {
                return 1.0;
            }
        }
    }

    @Override
    public String toString() {
        return status.name().toLowerCase();
    }

    public Weather clone() {
        Weather clone = new Weather();
        clone.status = status;
        return clone;
    }
}
