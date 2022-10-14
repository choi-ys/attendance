package io.sample.attendance.domain;

import static io.sample.attendance.validator.TimeValidator.validateStartAndEndTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AccessType(Type.FIELD)
public class TimeTable {
    public static final int MINUTE_PER_HOUR = 60;

    @Column
    private LocalDateTime startAt;

    @Column
    private LocalDateTime endAt;

    @Transient
    private WorkDuration workDuration;

    private TimeTable(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.workDuration = calculateDuration();
    }

    public static TimeTable of(LocalDateTime startAt, LocalDateTime endAt) {
        validateStartAndEndTime(startAt, endAt);
        return new TimeTable(startAt, endAt);
    }

    public WorkDuration calculateDuration() {
        int totalMinute = getWorkDurationByMinute();
        int hour = totalMinute / MINUTE_PER_HOUR;
        int minute = totalMinute % MINUTE_PER_HOUR;
        return WorkDuration.of(hour, minute);
    }

    public Duration between() {
        return Duration.between(startAt, endAt);
    }

    public int getWorkDurationByMinute() {
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
