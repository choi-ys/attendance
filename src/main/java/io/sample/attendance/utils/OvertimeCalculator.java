package io.sample.attendance.utils;

import io.sample.attendance.model.domain.Attendance;
import io.sample.attendance.model.domain.ExtraWork;
import io.sample.attendance.model.domain.ExtraWorkType;
import java.time.Duration;
import java.time.LocalDateTime;

public class OvertimeCalculator {
    public static final int DAILY_STATUTORY_ACTUAL_WORKING_HOUR = 8;
    public static final int MAX_BREAK_TIME_HOUR = 1;
    public static final int DAILY_STATUTORY_WORKING_HOUR = DAILY_STATUTORY_ACTUAL_WORKING_HOUR + MAX_BREAK_TIME_HOUR;
    public static final int DAILY_STATUTORY_WORKING_MINUTE = 540;

    public static boolean isOverTime(Attendance attendance) {
        Duration between = Duration.between(attendance.getStartAt(), attendance.getEndAt());
        return between.toMinutes() > DAILY_STATUTORY_WORKING_MINUTE;
    }

    public static ExtraWork extract(Attendance attendance) {
        return ExtraWork.of(attendance, findOvertimeStartAt(attendance.getStartAt()), attendance.getEndAt(), ExtraWorkType.OVERTIME);
    }

    private static LocalDateTime findOvertimeStartAt(LocalDateTime startAt) {
        return startAt.plusHours(DAILY_STATUTORY_WORKING_HOUR);
    }
}
