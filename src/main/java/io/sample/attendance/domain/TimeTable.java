package io.sample.attendance.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeTable {
    public static final int MINUTE_PER_HOUR = 60;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private TimeTable(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public static TimeTable of(LocalDateTime startAt, LocalDateTime endAt) {
        return new TimeTable(startAt, endAt);
    }

    public LocalTime getDurationOfTime() {
        Duration between = between();
        return LocalTime.of(between.toHoursPart(), between.toMinutesPart());
    }

    private Duration between() {
        return Duration.between(startAt, endAt);
    }

    public int getDurationByMinute() {
        LocalTime durationOfTime = getDurationOfTime();
        return hoursToMinutes(durationOfTime.getHour()) + durationOfTime.getMinute();
    }

    private int hoursToMinutes(int hour) {
        return hour * MINUTE_PER_HOUR;
    }
}
