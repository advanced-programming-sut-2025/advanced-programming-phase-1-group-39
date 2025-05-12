package models;

import models.Enums.WeatherStatus;

public class Weather {
    private WeatherStatus status;

    public void thor() {}

    public void setWeatherSunny() {
        status = WeatherStatus.SUNNY;
    }

    public void setStatus(WeatherStatus status) {
        this.status = status;
    }

    public WeatherStatus getStatus() {
        return status;
    }
}