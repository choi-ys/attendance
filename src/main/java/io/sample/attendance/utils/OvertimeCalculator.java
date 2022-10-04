package io.sample.attendance.utils;

import io.sample.attendance.domain.ExtraWork;
import io.sample.attendance.domain.ExtraWorkType;
import java.time.Duration;
import java.time.LocalDateTime;

public class OvertimeCalculator {
    public static final int DAILY_STATUTORY_ACTUAL_WORKING_HOUR = 8;
    public static final int MAX_BREAK_TIME_HOUR = 1;
    public static final int DAILY_STATUTORY_WORKING_HOUR = DAILY_STATUTORY_ACTUAL_WORKING_HOUR + MAX_BREAK_TIME_HOUR;
    public static final int DAILY_STATUTORY_WORKING_MINUTE = 540;

    public static boolean isOverTime(LocalDateTime startAt, LocalDateTime endAt) {
        Duration between = Duration.between(startAt, endAt);
        return between.toMinutes() > DAILY_STATUTORY_WORKING_MINUTE;
    }

    public static ExtraWork extract(LocalDateTime startAt, LocalDateTime endAt) {
        return ExtraWork.of(findOvertimeStartAt(startAt), endAt, ExtraWorkType.OVERTIME);
    }

    private static LocalDateTime findOvertimeStartAt(LocalDateTime startAt) {
        return startAt.plusHours(DAILY_STATUTORY_WORKING_HOUR);
    }
}
