package models;

import models.Enums.DayOfWeek;
import models.Enums.Season;

public class Time {
    private int day = 1;
    private int year = 1;
    private Season season = Season.SPRING;
    private DayOfWeek dayOfWeek = DayOfWeek.SATURDAY;
    private int hour = 9;

    public void goToNextDay() {
        addToDay(1);
        hour = 9;
    }

    public void addToHour(int hour) {
        this.hour += hour;
        if (this.hour > 23) {
            addToDay(this.hour / 24);
            this.hour %= 24;
        }
    }

    public void addToDay(int day) {
        this.day += day;
        addToWeekDay(this.day);
        if (this.day > 28) {
            addToSeason(this.day / 28);
            this.day -= 28;
        }
    }
    public void addToWeekDay(int day) {
        this.dayOfWeek = DayOfWeek.getDayByNumber((dayOfWeek.number + day)%7);
    }

    public void addToSeason(int number) {
        addToYear(number / 4);
        this.season = Season.getSeasonByNumber((season.number + number)%4);
    }

    public void addToYear(int year) {
        this.year += year;
    }


    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    public Season getSeason() {
        return season;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
}
