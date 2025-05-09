package models.Enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum WeatherStatus {
    SUNNY(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER),
    RAIN(Season.SPRING, Season.SUMMER, Season.FALL),
    STORM(Season.SPRING, Season.SUMMER, Season.FALL),
    SNOW(Season.WINTER);

    List<Season> seasons;

    WeatherStatus(Season... seasons) {
        this.seasons = Arrays.asList(seasons);
    }

    public static WeatherStatus getWeatherStatusByName(String name) {
        for (WeatherStatus status : WeatherStatus.values()) {
            if (status.name().equalsIgnoreCase(name)) {
                return status;
            }
        }
        return null;
    }

    public boolean canOccurInSeason(Season season) {
        return seasons.contains(season);
    }
}
