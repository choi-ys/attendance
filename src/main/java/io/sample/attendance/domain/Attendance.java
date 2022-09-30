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
    public static final int BASIC_PAY_PER_HOUR = 10000;

    private TimeTable timeTable;
    private LocalTime workingTime;
    private Overtime overtime;
    private NightShift nightShift;
    private int basicPay;
    private int totalPay;

    private Attendance(LocalDateTime startAt, LocalDateTime endAt) {
        this.timeTable = TimeTable.of(startAt, endAt);
        this.workingTime = calculateWorkingTime();
        this.overtime = Overtime.of(startAt, endAt);
        this.nightShift = NightShift.of(startAt, endAt);
        this.basicPay = calculateBasicPay();
        this.totalPay = calculateTotalPay();
    }

    private int calculateBasicPay() {
        return workingTime.getHour() * BASIC_PAY_PER_HOUR;
    }

    private int calculateTotalPay() {
        return basicPay + overtime.getExtraPay() + nightShift.getExtraPay();
    }

    public static Attendance of(LocalTime startTime, LocalTime endTime) {
        validateCommuteTime(startTime, endTime);
        LocalDate today = LocalDate.now();
        LocalDate endDate = calculateEndDate(startTime, endTime);
        return new Attendance(LocalDateTime.of(today, startTime), LocalDateTime.of(endDate, endTime));
    }

    private static void validateCommuteTime(LocalTime startTime, LocalTime endTime) {
        if (startTime.equals(endTime)) {
            throw new IllegalArgumentException(START_TIME_AND_END_TIME_IS_SAME_ERROR_MESSAGE);
        }
    }

    private static LocalDate calculateEndDate(LocalTime startTime, LocalTime endTime) {
        if (isNextDayEnd(startTime, endTime)) {
            return LocalDate.now().plusDays(1);
        }
        return LocalDate.now();
    }

    private static boolean isNextDayEnd(LocalTime startTime, LocalTime endTime) {
        return startTime.isAfter(endTime);
    }

    private LocalTime calculateWorkingTime() {
        Duration duration = Duration.between(timeTable.getStartAt(), timeTable.getEndAt());
        if (hasBreakTime(duration)) {
            duration = duration.minusHours(MAX_BREAK_TIME_HOUR);
        }
        return LocalTime.of(duration.toHoursPart(), duration.toMinutesPart());
    }

    private boolean hasBreakTime(Duration duration) {
        return duration.toHours() >= DAILY_STATUTORY_WORKING_HOUR;
    }

    public LocalDateTime getStartAt() {
        return timeTable.getStartAt();
    }

    public LocalDateTime getEndAt() {
        return timeTable.getEndAt();
    }

    public LocalTime getOvertimeWorkingTime() {
        return overtime.getWorkingTime();
    }

    public LocalTime getOvertimeStartAt() {
        return overtime.getStartTime();
    }

    public LocalTime getOvertimeEndAt() {
        return overtime.getEndTime();
    }

    public int getOvertimeExtraPay() {
        return overtime.getExtraPay();
    }

    public LocalTime getNightShiftWorkingTime() {
        return nightShift.getWorkingTime();
    }

    public LocalTime getNightShiftStartAt() {
        return nightShift.getStartTime();
    }

    public LocalTime getNightShiftEndAt() {
        return nightShift.getEndTime();
    }

    public int getNightShiftExtraPay() {
        return nightShift.getExtraPay();
    }
}
