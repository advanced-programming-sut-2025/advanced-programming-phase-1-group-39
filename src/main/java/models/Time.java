package models;

import models.Enums.DayOfWeek;
import models.Enums.Season;

public class Time {
    private int day = 1;
    private int year = 1;
    private Season season = Season.SPRING;
    private DayOfWeek dayOfWeek = DayOfWeek.SATURDAY;
    private int hour = 9;

    public Time() {}

    public Time(int day, int year, Season season, DayOfWeek dayOfWeek, int hour) {
        this.day = day;
        this.year = year;
        this.season = season;
        this.dayOfWeek = dayOfWeek;
        this.hour = hour;
    }

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
        addToWeekDay(day);
        if (this.day > 28) {
            addToSeason(this.day / 28);
            this.day = (this.day - 1) % 28 + 1;
        }
    }
    public void addToWeekDay(int day) {
        this.dayOfWeek = DayOfWeek.getDayByNumber((dayOfWeek.number + day)%7);
    }

    public void addToSeason(int number) {
        addToYear((season.number + number) / 4);
        this.season = Season.getSeasonByNumber((season.number + number)%4);
    }

    public void addToYear(int year) {
        this.year += year;
    }


    public int getHour() {
        return hour;
    }
    public String getHourText() {
        String end = hour <= 12 ? " AM" : " PM";
        return hour + end;
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

    public String getDayDetail() {
        return "Day " + day + "  " + dayOfWeek.name();
    }
    public String getDateDetail() {
        return "Year " + year + "  " + season.name();
    }

    public int allTimeByHour() {
        return (year-1)*(4 * 28 * 24) + (season.number - 1)*28*24 + (day - 1)*24 + hour;
    }

    public boolean isGreater(Time time) {
        return allTimeByHour() >= time.allTimeByHour();
    }

    public Time clone() {
        return new Time(this.day, this.year, this.season, this.dayOfWeek, this.hour);
    }
}