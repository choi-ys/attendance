package io.sample.attendance.utils;

import static org.hibernate.type.IntegerType.ZERO;

import io.sample.attendance.domain.Attendance;
import io.sample.attendance.domain.ExtraWork;
import io.sample.attendance.domain.ExtraWorkType;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class NightShiftCalculator {
    public static final int NIGHT_SHIFT_START_HOUR = 22;
    public static final int NIGHT_SHIFT_END_HOUR = 6;
    public static final LocalTime NIGHT_SHIFT_START_TIME = LocalTime.of(NIGHT_SHIFT_START_HOUR, ZERO);
    public static final LocalTime NIGHT_SHIFT_END_TIME = LocalTime.of(NIGHT_SHIFT_END_HOUR, ZERO);

    /**
     * @param attendance 일일 근태 정보
     * @return 야간 근무 여부
     * @implNote 야간 근무 여부를 판별 한다.
     * <pre>
     * {@code
     * 아래 케이스 중 하나라도 포함되는 경우 야간 근무로 판별
     * - 당일 퇴근이 아닌 경우
     * - 출근 혹은 퇴근 시간이 야간 근무 산정 범위에 포함되는 경우
     * }
     * </pre>
     */
    public static boolean isNightShift(Attendance attendance) {
        return isNotSameDayWork(attendance) || isIncludeStartOrEnd(attendance);
    }

    private static boolean isNotSameDayWork(Attendance attendance) {
        return !isSameDayWork(attendance);
    }

    /**
     * @param attendance 일일 근태 정보
     * @return 당일 퇴근 여부
     * @implNote 출근 일자와 퇴근 일자를 비교하여 당일 퇴근 여부를 판별한다.
     */
    private static boolean isSameDayWork(Attendance attendance) {
        return attendance.getStartAt().getDayOfMonth() == attendance.getEndAt().getDayOfMonth();
    }

    /**
     * @param attendance 일일 근태 정보
     * @return 출근 혹은 퇴근 시간이 야간 근무 산정 범위에 포함되는지 여부
     */
    private static boolean isIncludeStartOrEnd(Attendance attendance) {
        return isBetween(attendance.getStartAt().toLocalTime()) || isBetween(attendance.getEndAt().toLocalTime());
    }

    /**
     * @param target 출근 혹은 퇴근 시간 (hh:mm)
     * @return 주어진 시간이 야간 근무 산정 범위에 포함되는지 여부
     */
    private static boolean isBetween(LocalTime target) {
        return target.isAfter(NIGHT_SHIFT_START_TIME) || target.isBefore(NIGHT_SHIFT_END_TIME);
    }

    /**
     * @param attendance 일일 근태 정보
     * @return 야간 근무 구간
     * @implNote 출/퇴근 시간으로부터 근무 중 야간 근무에 해당하는 구간을 추출한다.
     */
    public static List<ExtraWork> extract(Attendance attendance) {
        List<LocalDateTime> hoursOfNightShiftRange = new ArrayList<>();
        List<Integer> nightShiftEndHoursIndices = new ArrayList<>();
        filterHoursOfNightShiftRange(attendance, hoursOfNightShiftRange, nightShiftEndHoursIndices);
        return extractNightShiftSections(attendance, hoursOfNightShiftRange, nightShiftEndHoursIndices);
    }

    /**
     * @param attendance                일일 근태 정보
     * @param hoursOfNightShiftRange    출근 시간부터 퇴근시간 까지의 근무 시간 중 야간 근무 산정 범위에 포함되는 시간대 목록
     * @param nightShiftEndHoursIndices 야간 근무 산정 범위에 포함되는 시간 대 배열의 원소 중 야간 근무 종료 시간 원소의 위치
     * @implNote 전체 근무 시간 중 야간 근무 구간의 시작/종료 시간 추출을 위해, 야간 근무 산정 범위에 포함되는 시간대만 필터링한다.
     * <pre>
     *     출근 : 2022-09-23 05:59
     *     퇴근 : 2022-09-24 01:00
     *     hoursOfNightShiftRange : 2022-09-23T05:00, `2022-09-23T06:00`, 2022-09-23T22:00, 2022-09-23T23:00, 2022-09-24T00:00
     *     nightShiftEndHoursIndices : 1
     * </pre>
     */
    private static void filterHoursOfNightShiftRange(
        Attendance attendance,
        List<LocalDateTime> hoursOfNightShiftRange,
        List<Integer> nightShiftEndHoursIndices
    ) {
        LocalDateTime startCondition = attendance.getStartAt().withMinute(0);
        LocalDateTime endAt = attendance.getEndAt();
        startCondition = checkFistCondition(startCondition);
        int index = 0;
        while (!startCondition.isAfter(endAt)) {
            LocalTime currentTime = startCondition.toLocalTime();
            if (isNightShiftHour(currentTime)) {
                hoursOfNightShiftRange.add(startCondition);
                if (isNightShiftEndingHour(currentTime.getHour())) {
                    nightShiftEndHoursIndices.add(index);
                }
                index++;
            }
            startCondition = startCondition.plusHours(1);
        }
    }

    /**
     * @param startCondition 전체 근무 시간 중 야간 근무 산정 범위에 포함되는 시간대 목록
     * @return 첫번쨰 원소가 처리된 시간대 목록
     * @apiNote 야간 근무 산정 범위에 포함되는 시간대를 필터링 한 후, 야간 근무 종료 시간이 6시 이므로, 첫번쨰 원소의 시간대가 6인지를 체크한다.
     */
    private static LocalDateTime checkFistCondition(LocalDateTime startCondition) {
        if (startCondition.getHour() == 6) {
            return startCondition.plusHours(1);
        }
        return startCondition;
    }

    private static boolean isNightShiftHour(LocalTime target) {
        return goeThanNightShiftStartTime(target) || loeThanNightShiftEndTime(target);
    }

    private static boolean goeThanNightShiftStartTime(LocalTime target) {
        return target.compareTo(NIGHT_SHIFT_START_TIME) != -1;
    }

    private static boolean loeThanNightShiftEndTime(LocalTime target) {
        return target.compareTo(NIGHT_SHIFT_END_TIME) != 1;
    }

    private static boolean isNightShiftEndingHour(int currentHour) {
        return currentHour == NIGHT_SHIFT_END_HOUR;
    }

    /**
     * @param attendance                일일 근태 정보
     * @param hoursOfNightShiftRange    출근 시간부터 퇴근시간 까지의 근무 시간 중 야간 근무 산정 범위에 포함되는 시간대 목록
     * @param nightShiftEndHoursIndices 야간 근무 산정 범위에 포함되는 시간 대 배열의 원소 중 야간 근무 종료 시간 원소의 위치
     * @return 야간 근무 구간 목록
     * @implNote 야간 근무 산정 범위에 포함된 시간대 목록을 이용하여 야간 근무 구간을 추출한다.
     */
    private static List<ExtraWork> extractNightShiftSections(
        Attendance attendance,
        List<LocalDateTime> hoursOfNightShiftRange,
        List<Integer> nightShiftEndHoursIndices
    ) {
        List<ExtraWork> extraWorks = new ArrayList<>();
        extraWorks.add(findFirstSection(attendance, hoursOfNightShiftRange, nightShiftEndHoursIndices));
        if (hasMoreSection(hoursOfNightShiftRange, nightShiftEndHoursIndices)) {
            extraWorks.add(findSecondSection(attendance, hoursOfNightShiftRange, nightShiftEndHoursIndices));
        }
        return extraWorks;
    }

    /**
     * @param attendance                일일 근태 정보
     * @param hoursOfNightShiftRange    출근 시간부터 퇴근시간 까지의 근무 시간 중 야간 근무 산정 범위에 포함되는 시간대 목록
     * @param nightShiftEndHoursIndices 야간 근무 산정 범위에 포함되는 시간 대 배열의 원소 중 야간 근무 종료 시간 원소의 위치
     * @return 첫번째 야간 근무 구간
     * @implNote 첫번째 야간 근무 구간을 추출한다.
     */
    private static ExtraWork findFirstSection(
        Attendance attendance,
        List<LocalDateTime> hoursOfNightShiftRange,
        List<Integer> nightShiftEndHoursIndices
    ) {
        LocalDateTime nightShiftStartAt = findFirstSectionStartAt(attendance.getStartAt(), hoursOfNightShiftRange);
        LocalDateTime nightShiftEndAt = findFirstSectionEndAt(attendance.getEndAt(), hoursOfNightShiftRange, nightShiftEndHoursIndices);
        return ExtraWork.of(attendance, nightShiftStartAt, nightShiftEndAt, ExtraWorkType.NIGHT_SHIFT);
    }

    /**
     * @param startAt                출근 시간 (YYYY-MM-DD hh:mm)
     * @param hoursOfNightShiftRange 출근 시간부터 퇴근시간 까지의 근무 시간 중 야간 근무 산정 범위에 포함되는 시간대 목록
     * @return 첫번째 야간 근무 구간의 시작 시간 (YYYY-MM-DD hh:mm)
     * @implNote 첫번째 야간 근무 구간의 시작 시간을 추출한다.
     * <pre>
     *     {@code
     *     출근 시간이 야간 근무 산정 범위에 포함되는 경우 : 출근 시간
     *     출근 시간이 야간 근무 산정 범위에 포함되지 않는 경우 : 필터링한 야간 근무 시간대의 첫번쟤 원소
     *     }
     * </pre>
     */
    private static LocalDateTime findFirstSectionStartAt(LocalDateTime startAt, List<LocalDateTime> hoursOfNightShiftRange) {
        if (isBetween(startAt.toLocalTime())) {
            return startAt;
        }
        return hoursOfNightShiftRange.get(0);
    }

    /**
     * @param endAt                     퇴근 시간 (YYYY-MM-DD hh:mm)
     * @param hoursOfNightShiftRange    출근 시간부터 퇴근시간 까지의 근무 시간 중 야간 근무 산정 범위에 포함되는 시간대 목록
     * @param nightShiftEndHoursIndices 야간 근무 산정 범위에 포함되는 시간대 배열의 원소 중 야간 근무 종료 시간 원소의 위치
     * @return 첫번째 야간 근무 구간의 종료 시간(YYYY-MM-DD hh:mm)
     * @implNote 첫번째 야간 근무의 종료 시간을 추출한다.
     * <pre>
     *     {@code
     *     필터링한 야간 근무 시간대 목록 중 6시가 존재 하지 않는 경우 : 퇴근 시간
     *     필터링한 야간 근무 시간대 목록 중 6시가 존재 하는 경우 : 6시
     *     }
     * </pre>
     */
    private static LocalDateTime findFirstSectionEndAt(
        LocalDateTime endAt,
        List<LocalDateTime> hoursOfNightShiftRange,
        List<Integer> nightShiftEndHoursIndices
    ) {
        if (nightShiftEndHoursIndices.isEmpty()) {
            return endAt;
        }
        return hoursOfNightShiftRange.get(nightShiftEndHoursIndices.get(0));
    }

    /**
     * @param hoursOfNightShiftRange    출근 시간부터 퇴근시간 까지의 근무 시간 중 야간 근무 산정 범위에 포함되는 시간대 목록
     * @param nightShiftEndHoursIndices 야간 근무 산정 범위에 포함되는 시간대 배열의 원소 중 야간 근무 종료 시간 원소의 위치
     * @return 두번째 야간근무 구간 존재 여부
     * @implNote 전체 근무 중 야간 근무 종료 시간이 야간 근무 산정 범위에 포함되는 시간대 배열의 마지막 원소인지 여부에 따라 두번째 야간 근무 구간이 있는지를 판별한다.
     * <pre>
     *     {@code
     *     출근 : 2022-09-23 05:59
     *     퇴근 : 2022-09-24 01:00
     *     hoursOfNightShiftRange : 2022-09-23T05:00, `2022-09-23T06:00`, 2022-09-23T22:00, 2022-09-23T23:00, 2022-09-24T00:00
     *     nightShiftEndHoursIndices : 1
     *     hoursOfNightShiftRange.size() - 1 = 4
     *     nightShiftEndHoursIndices.get(0) = 1
     *     두번째 야근 근무 구간 : 2022-09-23T22:00, ~ 2022-09-24T00:00
     *     }
     * </pre>
     */
    private static boolean hasMoreSection(List<LocalDateTime> hoursOfNightShiftRange, List<Integer> nightShiftEndHoursIndices) {
        return !nightShiftEndHoursIndices.isEmpty() && (hoursOfNightShiftRange.size() - 1 != nightShiftEndHoursIndices.get(0));
    }

    /**
     * @param attendance                일일 근태 정보
     * @param hoursOfNightShiftRange    출근 시간부터 퇴근시간 까지의 근무 시간 중 야간 근무 산정 범위에 포함되는 시간대 목록
     * @param nightShiftEndHoursIndices 야간 근무 산정 범위에 포함되는 시간 대 배열의 원소 중 야간 근무 종료 시간 원소의 위치
     * @return 두번째 야간 근무 구간
     * @implNote 두번째 야간 근무 구간을 추출한다.
     * <pre>
     *     {@code
     *     두번째 야간 근무 구간의 시작 시간 : 야간 근무 산정 범위에 포함되는 시간대 목록 중 야간 근무 종료 시간의 다음 원소
     *     두번째 야간 근무 구간의 종료 시간 :
     *      - 퇴근 시간이 야간 근무 산정 범위에 포함되는 경우 : 퇴근 시간
     *      - 퇴근 시간이 야간 근무 산정 범위에 포함되지 않는 경우 : 퇴근 일자의 야간 근무 종료 시간
     *      }
     * </pre>
     */
    private static ExtraWork findSecondSection(
        Attendance attendance,
        List<LocalDateTime> hoursOfNightShiftRange,
        List<Integer> nightShiftEndHoursIndices
    ) {
        LocalDateTime nightShiftStartAt = hoursOfNightShiftRange.get(nightShiftEndHoursIndices.get(0) + 1);
        LocalDateTime endAt = attendance.getEndAt();
        if (isNightShiftHour(endAt.toLocalTime())) {
            return ExtraWork.of(attendance, nightShiftStartAt, endAt, ExtraWorkType.NIGHT_SHIFT);
        }
        return ExtraWork.of(attendance, nightShiftStartAt, endAt.withHour(6).withMinute(0), ExtraWorkType.NIGHT_SHIFT);
    }
}
