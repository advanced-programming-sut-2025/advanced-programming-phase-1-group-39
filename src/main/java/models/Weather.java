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

    }

    public void setWeatherSunny() {
        status = WeatherStatus.SUNNY;
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

    @Override
    public String toString() {
        return status.name().toLowerCase();
    }
}
