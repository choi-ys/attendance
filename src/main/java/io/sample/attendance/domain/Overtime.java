package io.sample.attendance.domain;

import static io.sample.attendance.domain.Attendance.DAILY_STATUTORY_WORKING_HOUR;
import static io.sample.attendance.domain.Attendance.DAILY_STATUTORY_WORKING_MINUTE;
import static org.hibernate.type.IntegerType.ZERO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class Overtime {
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime duration;

    private Overtime(LocalTime startTime, LocalTime endTime, LocalTime duration) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }

    public static Overtime of(LocalDateTime startAt, LocalDateTime endAt) {
        Duration between = Duration.between(startAt, endAt);
        if (isNotOvertime(between)) {
            return notExistOvertime();
        }
        LocalTime duration = getDuration(between);
        LocalTime overtimeStartTime = getOvertimeStartTime(startAt);
        return new Overtime(overtimeStartTime, endAt.toLocalTime(), duration);
    }

    private static LocalTime getDuration(Duration between) {
        return LocalTime.of(between.toHoursPart() - DAILY_STATUTORY_WORKING_HOUR, between.toMinutesPart());
    }

    public static Overtime notExistOvertime() {
        LocalTime zero = LocalTime.MIDNIGHT;
        return new Overtime(zero, zero, zero);
    }

    private static boolean isOvertime(Duration between) {
        return between.toMinutes() - DAILY_STATUTORY_WORKING_MINUTE > ZERO;
    }

    private static boolean isNotOvertime(Duration between) {
        return !isOvertime(between);
    }

    private static LocalTime getOvertimeStartTime(LocalDateTime startAt) {
        return startAt.plusHours(DAILY_STATUTORY_WORKING_HOUR).toLocalTime();
    }
}
