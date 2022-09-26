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
    public static final int EXTRA_PAY_PER_MINUTE = 100;
    public static final int ONE_HOUR_BY_MINUTE = 60;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime duration;
    private int extraPay;

    private Overtime(LocalTime startTime, LocalTime endTime, LocalTime duration, int extraPay) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.extraPay = extraPay;
    }

    public static Overtime of(LocalDateTime startAt, LocalDateTime endAt) {
        Duration between = Duration.between(startAt, endAt);
        if (isNotOvertime(between)) {
            return notExistOvertime();
        }
        LocalTime duration = calculateDuration(between);
        return new Overtime(getOvertimeStartTime(startAt), endAt.toLocalTime(), duration, calculateExtraPay(duration));
    }

    private static int calculateExtraPay(LocalTime duration) {
        int totalOvertimeMinute = (duration.getHour() * ONE_HOUR_BY_MINUTE) + duration.getMinute();
        return totalOvertimeMinute * EXTRA_PAY_PER_MINUTE;
    }

    private static LocalTime calculateDuration(Duration between) {
        return LocalTime.of(between.toHoursPart() - DAILY_STATUTORY_WORKING_HOUR, between.toMinutesPart());
    }

    public static Overtime notExistOvertime() {
        final LocalTime zero = LocalTime.MIDNIGHT;
        return new Overtime(zero, zero, zero, ZERO);
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
