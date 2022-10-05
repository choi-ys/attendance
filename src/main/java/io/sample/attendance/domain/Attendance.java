package io.sample.attendance.domain;

import static io.sample.attendance.validator.TimeValidator.validateStartAndEndTime;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Attendance {
    public static final int DAILY_STATUTORY_ACTUAL_WORKING_HOUR = 8;
    public static final int MAX_BREAK_TIME_HOUR = 1;
    public static final int DAILY_STATUTORY_WORKING_HOUR = DAILY_STATUTORY_ACTUAL_WORKING_HOUR + MAX_BREAK_TIME_HOUR;
    public static final int DAILY_STATUTORY_WORKING_MINUTE = 540;
    public static final int BASIC_PAY_PER_HOUR = 10000;

    private TimeTable timeTable;
    private WorkDuration workDuration;
    private ExtraWorks extraWorks;
    private int basicPay;
    private int totalPay;

    private Attendance(LocalDateTime startAt, LocalDateTime endAt) {
        this.timeTable = TimeTable.of(startAt, endAt);
        this.workDuration = timeTable.getWorkDuration();
        this.extraWorks = new ExtraWorks(startAt, endAt);
        this.basicPay = calculateBasicPay();
        this.totalPay = calculateTotalPay();
    }

    public static Attendance of(LocalDateTime startAt, LocalDateTime endAt) {
        validateStartAndEndTime(startAt, endAt);
        return new Attendance(startAt, endAt);
    }

    private int calculateBasicPay() {
        return workDuration.getHour() * BASIC_PAY_PER_HOUR;
    }

    private int calculateTotalPay() {
        return basicPay + extraWorks.getTotalExtraPay();
    }
}
