package io.sample.attendance.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class Attendance {
    public static final String START_TIME_AND_END_TIME_IS_SAME_ERROR_MESSAGE = "출근시간과 퇴근시간은 같을 수 없습니다.";
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
}
