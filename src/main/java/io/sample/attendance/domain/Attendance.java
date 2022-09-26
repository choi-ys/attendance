package io.sample.attendance.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import lombok.Getter;

@Getter
public class Attendance {
    public static final String START_TIME_AND_END_TIME_IS_SAME_ERROR_MESSAGE = "출근시간과 퇴근시간은 같을 수 없습니다.";
    public static final int DAILY_STATUTORY_ACTUAL_WORKING_HOUR = 8;
    public static final int MAX_BREAK_TIME_HOUR = 1;
    public static final int DAILY_STATUTORY_WORKING_HOUR = DAILY_STATUTORY_ACTUAL_WORKING_HOUR + MAX_BREAK_TIME_HOUR;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private Attendance(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public static Attendance of(LocalTime startTime, LocalTime endTime) {
        validateCommuteTime(startTime, endTime);
        LocalDate startDate = LocalDate.now();
        if (isNextDayEnd(startTime, endTime)) {
            startDate = startDate.plusDays(1);
        }
        return new Attendance(LocalDateTime.of(startDate, startTime), LocalDateTime.of(startDate, endTime));
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
        int workingTimeByMinute = getTotalWorkingTimeByMinute();
        int workingHour = workingTimeByMinute / 60;
        int workingMinute = workingTimeByMinute % 60;
        if (hasBreakTime(workingHour)) {
            workingHour -= MAX_BREAK_TIME_HOUR;
        }
        return LocalTime.of(workingHour, workingMinute);
    }

    private int getTotalWorkingTimeByMinute() {
        return (int) ChronoUnit.MINUTES.between(startAt, endAt);
    }

    private boolean hasBreakTime(int workingHour) {
        return workingHour >= DAILY_STATUTORY_WORKING_HOUR;
    }
}
