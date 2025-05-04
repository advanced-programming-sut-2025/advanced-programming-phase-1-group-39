package models;

import models.Enums.WeatherStatus;

public class Weather {
    WeatherStatus status;

    public void thor() {}

    public void setStatus(WeatherStatus status) {
        this.status = status;
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
}
