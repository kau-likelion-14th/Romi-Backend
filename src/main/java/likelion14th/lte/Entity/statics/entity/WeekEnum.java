package likelion14th.lte.Entity.statics.entity;

import java.time.DayOfWeek;

public enum WeekEnum {
    MON, TUE, WED, THU, FRI, SAT, SUN;


    public DayOfWeek toDayOfWeek() {
        return switch (this) {
            case MON -> DayOfWeek.MONDAY;
            case TUE -> DayOfWeek.TUESDAY;
            case WED -> DayOfWeek.WEDNESDAY;
            case THU -> DayOfWeek.THURSDAY;
            case FRI -> DayOfWeek.FRIDAY;
            case SAT -> DayOfWeek.SATURDAY;
            case SUN -> DayOfWeek.SUNDAY;
        };
    }
}