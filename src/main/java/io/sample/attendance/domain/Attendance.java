package io.sample.attendance.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class Attendance {
    public static final String START_TIME_AND_END_TIME_IS_SAME_ERROR_MESSAGE = "출근시간과 퇴근시간은 같을 수 없습니다.";
    public static final int DAILY_STATUTORY_ACTUAL_WORKING_HOUR = 8;
    public static final int MAX_BREAK_TIME_HOUR = 1;
    public static final int DAILY_STATUTORY_WORKING_HOUR = DAILY_STATUTORY_ACTUAL_WORKING_HOUR + MAX_BREAK_TIME_HOUR;
    public static final int DAILY_STATUTORY_WORKING_MINUTE = 540;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private Attendance(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public static Attendance of(LocalTime startTime, LocalTime endTime) {
        validateCommuteTime(startTime, endTime);
        LocalDate today = LocalDate.now();
        LocalDate endDate = getEndDate(startTime, endTime);
        return new Attendance(LocalDateTime.of(today, startTime), LocalDateTime.of(endDate, endTime));
    }

    private static LocalDate getEndDate(LocalTime startTime, LocalTime endTime) {
        if (isNextDayEnd(startTime, endTime)) {
            return LocalDate.now().plusDays(1);
        }
        return LocalDate.now();
    }

    private static void validateCommuteTime(LocalTime startTime, LocalTime endTime) {
        if (startTime.equals(endTime)) {
            throw new IllegalArgumentException(START_TIME_AND_END_TIME_IS_SAME_ERROR_MESSAGE);
        }
    }

    private static boolean isNextDayEnd(LocalTime startTime, LocalTime endTime) {
        return startTime.isAfter(endTime);
    }

    public LocalTime getWorkingTime() {
        Duration between = Duration.between(startAt, endAt);
        if (hasBreakTime(between)) {
            between = between.minusHours(MAX_BREAK_TIME_HOUR);
        }
        return LocalTime.of(between.toHoursPart(), between.toMinutesPart());
    }

    private boolean hasBreakTime(Duration between) {
        return between.toHours() >= DAILY_STATUTORY_WORKING_HOUR;
    }
}
