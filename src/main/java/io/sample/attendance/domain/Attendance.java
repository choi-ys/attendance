package io.sample.attendance.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Attendance {
    public static final String START_AT_AND_END_AT_IS_SAME_ERROR_MESSAGE = "출근시간과 퇴근시간은 같을 수 없습니다.";
    public static final String END_AT_IS_EARLIER_THAN_START_AT_ERROR_MESSAGE = "퇴근시간이 출근시간 보다 빠를 수 없습니다.";
    public static final int DAILY_STATUTORY_ACTUAL_WORKING_HOUR = 8;
    public static final int MAX_BREAK_TIME_HOUR = 1;
    public static final int DAILY_STATUTORY_WORKING_HOUR = DAILY_STATUTORY_ACTUAL_WORKING_HOUR + MAX_BREAK_TIME_HOUR;
    public static final int DAILY_STATUTORY_WORKING_MINUTE = 540;
    public static final int BASIC_PAY_PER_HOUR = 10000;

    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private WorkDuration workDuration;
    private ExtraWorks extraWorks;
    private int basicPay;
    private int totalPay;

    private Attendance(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.workDuration = calculateWorkingTime();
        this.extraWorks = new ExtraWorks(startAt, endAt);
        this.basicPay = calculateBasicPay();
        this.totalPay = calculateTotalPay();
    }

    public static Attendance of(LocalDateTime startAt, LocalDateTime endAt) {
        validateCommuteTime(startAt, endAt);
        return new Attendance(startAt, endAt);
    }

    private int calculateBasicPay() {
        return workDuration.getHour() * BASIC_PAY_PER_HOUR;
    }

    private int calculateTotalPay() {
        return basicPay + extraWorks.getTotalExtraPay();
    }

    private WorkDuration calculateWorkingTime() {
        Duration between = Duration.between(startAt, endAt);
        int totalMinute = (int) between.toMinutes();
        int hour = totalMinute / 60;
        int minute = totalMinute % 60;
        return WorkDuration.of(hour, minute);
    }

    private static void validateCommuteTime(LocalDateTime startAt, LocalDateTime endAt) {
        validateCommuteTimeIsSame(startAt, endAt);
        validateEndAtIsEarlierThanStartAt(startAt, endAt);
    }

    private static void validateCommuteTimeIsSame(LocalDateTime startAt, LocalDateTime endAt) {
        if (startAt.equals(endAt)) {
            throw new IllegalArgumentException(START_AT_AND_END_AT_IS_SAME_ERROR_MESSAGE);
        }
    }

    private static void validateEndAtIsEarlierThanStartAt(LocalDateTime startAt, LocalDateTime endAt) {
        if (endAt.isBefore(startAt)) {
            throw new IllegalArgumentException(END_AT_IS_EARLIER_THAN_START_AT_ERROR_MESSAGE);
        }
    }
}
