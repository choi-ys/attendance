package io.sample.attendance.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class TimeTable {
    public static final int MINUTE_PER_HOUR = 60;
    public static final int HOUR_PER_DAY = 24;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private TimeTable(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public static TimeTable of(LocalDateTime startAt, LocalDateTime endAt) {
        return new TimeTable(startAt, endAt);
    }

    public WorkDuration getDuration() {
        Duration between = between();
        return WorkDuration.of((int) between.toHours(), between.toMinutesPart());
    }

    private Duration between() {
        return Duration.between(startAt, endAt);
    }

    public int getDurationByMinute() {
        return (int) between().toMinutes();
    }
}
