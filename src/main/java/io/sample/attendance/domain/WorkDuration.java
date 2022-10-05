package io.sample.attendance.domain;

import static org.hibernate.type.IntegerType.ZERO;

import java.util.Objects;
import lombok.Getter;

@Getter
public class WorkDuration {
    public static final int MAXIMUM_HOURS = 28;
    public static final String OVER_MAXIMUM_WORKING_HOURS_ERROR_MESSAGE = "근무 소요 시간은 28시간을 초과할 수 없습니다.";
    private int hour;
    private int minute;

    private WorkDuration(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public static WorkDuration empty() {
        return new WorkDuration(ZERO, ZERO);
    }

    public static WorkDuration of(int hour, int minute) {
        validateWorkingTime(hour, minute);
        return new WorkDuration(hour, minute);
    }

    private static void validateWorkingTime(int hour, int minute) {
        if (isOverThanMaximumWorkingDuration(hour, minute)) {
            throw new IllegalArgumentException(OVER_MAXIMUM_WORKING_HOURS_ERROR_MESSAGE);
        }
    }

    private static boolean isOverThanMaximumWorkingDuration(int hour, int minute) {
        return hour >= MAXIMUM_HOURS && minute > ZERO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkDuration that = (WorkDuration) o;
        return hour == that.hour &&
            minute == that.minute;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hour, minute);
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }
}
