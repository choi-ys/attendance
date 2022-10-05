package io.sample.attendance.domain;

import static io.sample.attendance.validator.TimeValidator.validateStartAndEndTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
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
        validateStartAndEndTime(startAt, endAt);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeTable timeTable = (TimeTable) o;
        return Objects.equals(startAt, timeTable.startAt) &&
            Objects.equals(endAt, timeTable.endAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startAt, endAt);
    }
}
