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
    private LocalTime workingTime;
    private int extraPay;

    private Overtime(LocalTime startTime, LocalTime endTime, LocalTime workingTime, int extraPay) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.workingTime = workingTime;
        this.extraPay = extraPay;
    }

    public static Overtime of(LocalDateTime startAt, LocalDateTime endAt) {
        Duration duration = Duration.between(startAt, endAt);
        if (isNotOvertime(duration)) {
            return notExistOvertime();
        }
        LocalTime workingTime = calculateWorkingTime(duration);
        return new Overtime(getOvertimeStartTime(startAt), endAt.toLocalTime(), workingTime, calculateExtraPay(workingTime));
    }

    private static int calculateExtraPay(LocalTime duration) {
        int totalOvertimeMinute = (duration.getHour() * ONE_HOUR_BY_MINUTE) + duration.getMinute();
        return totalOvertimeMinute * EXTRA_PAY_PER_MINUTE;
    }

    private static LocalTime calculateWorkingTime(Duration duration) {
        return LocalTime.of(duration.toHoursPart() - DAILY_STATUTORY_WORKING_HOUR, duration.toMinutesPart());
    }

    public static Overtime notExistOvertime() {
        final LocalTime zero = LocalTime.MIDNIGHT;
        return new Overtime(zero, zero, zero, ZERO);
    }

    private static boolean isOvertime(Duration duration) {
        return duration.toMinutes() - DAILY_STATUTORY_WORKING_MINUTE > ZERO;
    }

    private static boolean isNotOvertime(Duration duration) {
        return !isOvertime(duration);
    }

    private static LocalTime getOvertimeStartTime(LocalDateTime startAt) {
        return startAt.plusHours(DAILY_STATUTORY_WORKING_HOUR).toLocalTime();
    }
}
