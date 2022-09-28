package io.sample.attendance.utils;

import static org.hibernate.type.IntegerType.ZERO;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class NightShiftCalculationUtils {
    public static final int NIGHT_SHIFT_START_HOUR = 22;
    public static final int NIGHT_SHIFT_END_HOUR = 6;
    public static final LocalTime NIGHT_SHIFT_START_TIME = LocalTime.of(NIGHT_SHIFT_START_HOUR, ZERO);
    public static final LocalTime NIGHT_SHIFT_END_TIME = LocalTime.of(NIGHT_SHIFT_END_HOUR, ZERO);

    /**
     * 야간 근무 여부를 판별 한다.
     *
     * @param startAt 출근 시간
     * @param endAt   퇴근 시간
     * @return 야간 근무 여부
     * @implNote <pre>
     * {@code
     * - 당일 퇴근이 아닌 경우
     * - 당일 연속된 야간 근무인 경우
     * - 당일 연속되지 않은 야간 근무인 경우
     * }
     * </pre>
     */
    public static boolean isNightShift(LocalDateTime startAt, LocalDateTime endAt) {
        return isNotSameWorkDay(startAt, endAt)
            || isContinuous(startAt, endAt)
            || isNotContinuous(startAt, endAt);
    }

    /**
     * 당일 퇴근 여부 판별
     *
     * @param startAt 근무 시작 일자 (YYYY-MM-DD hh:mm)
     * @param endAt   근무 종료 일자 (YYYY-MM-DD hh:mm)
     * @return 당일 퇴근 여부
     * @implNote 출근 일자와 퇴근 일자를 비교하여 당일 퇴근 여부를 판별한다.
     */
    public static boolean isSameWorkDay(LocalDateTime startAt, LocalDateTime endAt) {
        return startAt.getDayOfMonth() == endAt.getDayOfMonth();
    }

    private static boolean isNotSameWorkDay(LocalDateTime startAt, LocalDateTime endAt) {
        return !isSameWorkDay(startAt, endAt);
    }

    /**
     * 당일 연속된 야간 근무 여부 판별
     *
     * @param startAt 출근 시간
     * @param endAt   퇴근 시간
     * @return 연속된 야간 근무 여부
     * @implNote 당일 퇴근 이고, 출근 혹은 퇴근 시간 중 하나만 야간 근무 산정 범위에 포함되는지 여부를 판별한다.
     */
    public static boolean isContinuous(LocalDateTime startAt, LocalDateTime endAt) {
        return isSameWorkDay(startAt, endAt) && isIncludeOnlyOneInNightShiftTImeRange(startAt, endAt);
    }

    /**
     * 당일 연속되지 않은 야간 근무 여부 판별
     *
     * @param startAt 출근 시간
     * @param endAt   퇴근 시간
     * @return 연속되지 않은 야간 근무 여부
     * @implNote 당일 퇴근 이고, 출/퇴근 시간 모두 야간 근무 산정 범위에 포함되는지 여부를 판별한다.
     */
    public static boolean isNotContinuous(LocalDateTime startAt, LocalDateTime endAt) {
        return isSameWorkDay(startAt, endAt) && isIncludeAllInNightShiftTImeRange(startAt, endAt);
    }

    /**
     * 출근 혹은 퇴근 시간 중 하나만 야간 근무 산정 범위에 포함되는지 여부를 판별
     *
     * @param startAt 출근 시간
     * @param endAt   퇴근 시간
     * @return 출근 혹은 퇴근 시간 중 하나만 야간 근무 산정 범위에 포함되는지 여부
     * @implNote XOR 연산을 이용하여 출근 혹은 퇴근 시간 중 하나만 야간 근무 산정 범위에 포함되는지 여부를 판별한다
     */
    public static boolean isIncludeOnlyOneInNightShiftTImeRange(LocalDateTime startAt, LocalDateTime endAt) {
        return isIncludeInNightShiftTimeRange(startAt.toLocalTime()) ^ isIncludeInNightShiftTimeRange(endAt.toLocalTime());
    }

    /**
     * 출/퇴근 시간 모두 야간 근무 산정 범위에 포함되는지 여부를 판별
     *
     * @param startAt 출근 시간
     * @param endAt   퇴근 시간
     * @return 출/퇴근 시간 모두 야간 근무 산정 범위에 포함되는지 여부
     * @implNote AND 연산을 이용하여 출/퇴근 시간 모두 야간 근무 산정 범위에 포함되는지 여부를 판별한다
     */
    public static boolean isIncludeAllInNightShiftTImeRange(LocalDateTime startAt, LocalDateTime endAt) {
        return isIncludeInNightShiftTimeRange(startAt.toLocalTime()) && isIncludeInNightShiftTimeRange(endAt.toLocalTime());
    }

    /**
     * @param target 출근 혹은 퇴근 시간
     * @return 주어진 시간이 야간 근무 산정 범위에 포함되는지 여부를 판별
     */
    public static boolean isIncludeInNightShiftTimeRange(LocalTime target) {
        return target.isAfter(NIGHT_SHIFT_START_TIME) || target.isBefore(NIGHT_SHIFT_END_TIME);
    }
}
