package models.Enums;

public enum DayOfWeek {
    SATURDAY(0),
    SUNDAY(1),
    MONDAY(2),
    TUESDAY(3),
    WEDNESDAY(4),
    THURSDAY(5),
    FRIDAY(6);

    public int number;

    DayOfWeek(int number) {
        this.number = number;
    }

    public static DayOfWeek getDayByNumber(int number) {
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day.number == number) {
                return day;
            }
        }
        return null;
    }
}