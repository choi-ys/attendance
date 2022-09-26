package io.sample.attendance.domain;

import static org.hibernate.type.IntegerType.ZERO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import lombok.Getter;

@Getter
public class NightShift {
    public static final int NIGHT_SHIFT_START_HOUR = 22;
    public static final LocalTime NIGHT_SHIFT_START_TIME = LocalTime.of(NIGHT_SHIFT_START_HOUR, 0);
    public static final int NIGHT_SHIFT_END_HOUR = 6;
    public static final LocalTime NIGHT_SHIFT_END_TIME = LocalTime.of(NIGHT_SHIFT_END_HOUR, 0);
    public static final int[] NIGHT_SHIFT_HOUR_ARR = {22, 23, 0, 1, 2, 3, 4, 5, 6};
    public static final int EXTRA_PAY_PER_MINUTE = 150;

    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime duration;
    private int extraPay;

    private NightShift(LocalTime startTime, LocalTime endTime, LocalTime duration, int extraPay) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.extraPay = extraPay;
    }

    public static NightShift of(LocalDateTime startAt, LocalDateTime endAt) {
        if (isNotNightShift(startAt, endAt)) {
            return notExistNightShift();
        }
        LocalDateTime overtimeStartAt = getNightShiftStartTime(startAt);
        LocalDateTime overtimeEndAt = getNightShiftEndTime(endAt);
        Duration between = Duration.between(overtimeStartAt, overtimeEndAt);
        return new NightShift(overtimeStartAt.toLocalTime(), overtimeEndAt.toLocalTime(), calculateDuration(between), calculateExtraPay(between));
    }

    private static int calculateExtraPay(Duration between) {
        return (int) (between.toMinutes() * EXTRA_PAY_PER_MINUTE);
    }

    private static LocalTime calculateDuration(Duration between) {
        return LocalTime.of(between.toHoursPart(), between.toMinutesPart());
    }

    private static LocalDateTime getNightShiftStartTime(LocalDateTime startAt) {
        if (contain(startAt.getHour())) {
            return startAt;
        }
        return LocalDateTime.of(startAt.toLocalDate(), NIGHT_SHIFT_START_TIME);
    }

    private static LocalDateTime getNightShiftEndTime(LocalDateTime endAt) {
        if (contain(endAt.getHour())) {
            return endAt;
        }
        return LocalDateTime.of(endAt.toLocalDate(), NIGHT_SHIFT_END_TIME);
    }

    private static boolean contain(int hour) {
        return Arrays.stream(NIGHT_SHIFT_HOUR_ARR).anyMatch(it -> it == hour);
    }

    private static NightShift notExistNightShift() {
        final LocalTime zero = LocalTime.MIDNIGHT;
        return new NightShift(zero, zero, zero, ZERO);
    }

    private static boolean isNotNightShift(LocalDateTime startAt, LocalDateTime endAt) {
        return !isNightShift(startAt, endAt);
    }

    private static boolean isNightShift(LocalDateTime startAt, LocalDateTime endAt) {
        return isStartAtDayBeforeEndAtDay(startAt, endAt) ||
            isContainNightShiftRange(startAt, endAt);
    }

    private static boolean isContainNightShiftRange(LocalDateTime startAt, LocalDateTime endAt) {
        return isBetween(startAt.toLocalTime()) || isBetween(endAt.toLocalTime());
    }

    private static boolean isBetween(LocalTime targetTime) {
        return targetTime.isAfter(NIGHT_SHIFT_START_TIME) || targetTime.isBefore(NIGHT_SHIFT_END_TIME);
    }

    private static boolean isStartAtDayBeforeEndAtDay(LocalDateTime startAt, LocalDateTime endAt) {
        return startAt.getDayOfMonth() == endAt.getDayOfMonth() - 1;
    }
}
