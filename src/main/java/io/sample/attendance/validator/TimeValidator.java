package io.sample.attendance.validator;

import java.time.LocalDateTime;

public class TimeValidator {
    public static final String START_AT_AND_END_AT_IS_SAME_ERROR_MESSAGE = "출근시간과 퇴근시간은 같을 수 없습니다.";
    public static final String END_AT_IS_EARLIER_THAN_START_AT_ERROR_MESSAGE = "퇴근시간이 출근시간 보다 빠를 수 없습니다.";
    public static final String INVALID_VALUE_ERROR_MESSAGE = "값이 올바르지 않습니다.";

    public static void validateStartAndEndTime(LocalDateTime startAt, LocalDateTime endAt) {
        validateNull(startAt, endAt);
        validateEqual(startAt, endAt);
        validateTimeSequence(startAt, endAt);
    }

    private static void validateEqual(LocalDateTime startAt, LocalDateTime endAt) {
        if (startAt.equals(endAt)) {
            throw new IllegalArgumentException(START_AT_AND_END_AT_IS_SAME_ERROR_MESSAGE);
        }
    }

    private static void validateTimeSequence(LocalDateTime startAt, LocalDateTime endAt) {
        if (endAt.isBefore(startAt)) {
            throw new IllegalArgumentException(END_AT_IS_EARLIER_THAN_START_AT_ERROR_MESSAGE);
        }
    }

    private static void validateNull(LocalDateTime startAt, LocalDateTime endAt) {
        checkNull(startAt);
        checkNull(endAt);
    }

    private static void checkNull(LocalDateTime target) {
        if (target == null) {
            throw new IllegalArgumentException(INVALID_VALUE_ERROR_MESSAGE);
        }
    }
}
