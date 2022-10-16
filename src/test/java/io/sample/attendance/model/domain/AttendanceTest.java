package io.sample.attendance.model.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hibernate.type.IntegerType.ZERO;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.sample.attendance.exception.InValidTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Domain:Attendance")
class AttendanceTest {
    private final LocalDate today = LocalDate.now();
    final LocalDate nextDay = today.plusDays(1);

    @ParameterizedTest(name = "[Case#{index}]{0} : {1} ~ {2}")
    @MethodSource
    @DisplayName("[예외] 출근 시간과 퇴근 시간이 옳바르지 않은 경우")
    public void throwException_WhenStartAtAndEndAtIsInValid(
        final String description,
        final LocalDateTime startAt,
        final LocalDateTime endAt
    ) {
        // When & Then
        assertThatExceptionOfType(InValidTimeException.class)
            .as(description)
            .isThrownBy(() -> Attendance.of(startAt, endAt));
    }

    private static Stream<Arguments> throwException_WhenStartAtAndEndAtIsInValid() {
        final LocalDateTime now = LocalDateTime.now();
        return Stream.of(
            Arguments.of("출근 시간과 퇴근시간이 null인 경우", null, null),
            Arguments.of("퇴근 시간이 null인 경우", now, null),
            Arguments.of("출근 시간이 null인 경우", null, now),
            Arguments.of("출근 시간과 퇴근 시간이 같은 경우", now, now),
            Arguments.of("퇴근 시간이 출근 시간보다 빠른 경우", now, now.minusSeconds(1))
        );
    }

    @Test
    @DisplayName("추가 근무가 없는 출결의 급여 산출")
    public void attendance_withoutAnyExtraWorks() {
        // Given
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(9, ZERO));
        final LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(18, ZERO));

        // When
        Attendance given = Attendance.of(startAt, endAt);

        // Then
        ExtraWorks extraWorks = given.getExtraWorks();
        assertAll(
            () -> assertThat(given.getTotalPay()).as("급여 총액").isEqualTo(90000),
            () -> assertThat(given.getBasicPay()).as("기본급").isEqualTo(90000),
            () -> assertThat(given.getWorkDuration()).as("총 근로 시간").isEqualTo(WorkDuration.of(9, 0)),
            () -> {
                assertThat(extraWorks.getTotalExtraPay()).as("총 추가 근무 수당").isEqualTo(0);
                assertThat(extraWorks.getSize()).as("추가 근무 건수").isEqualTo(0);
                assertThat(extraWorks.getExtraWorkTypes()).as("추가 근무 타입").isEmpty();
                assertThat(extraWorks.getOverTime()).as("연장 근무 기록").isNull();
                assertThat(extraWorks.getNightShifts()).as("야간 근무 기록").isEmpty();
            }
        );
    }

    @Test
    @DisplayName("연장 근무가 포함된 출결의 급여 산출")
    public void attendance_containsOvertime() {
        // Given
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(9, ZERO));
        final LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(22, ZERO));

        // When
        Attendance given = Attendance.of(startAt, endAt);

        // Then
        ExtraWorks extraWorks = given.getExtraWorks();
        ExtraWork overTime = extraWorks.getOverTime();
        assertAll(
            () -> assertThat(given.getTotalPay()).as("급여 총액").isEqualTo(154000),
            () -> assertThat(given.getBasicPay()).as("기본급").isEqualTo(130000),
            () -> assertThat(given.getWorkDuration()).as("총 근로 시간").isEqualTo(WorkDuration.of(13, 0)),
            () -> {
                assertThat(extraWorks.getTotalExtraPay()).as("총 추가 근무 수당").isEqualTo(24000);
                assertThat(extraWorks.getSize()).as("추가 근무 건수").isEqualTo(1);
                assertThat(extraWorks.getExtraWorkTypes()).as("추가 근무 타입").containsOnly(ExtraWorkType.OVERTIME);
                assertThat(extraWorks.getOverTime()).as("연장 근무 기록").isNotNull();
                assertThat(extraWorks.getNightShifts()).as("야간 근무 기록").isEmpty();
            },
            () -> {
                TimeTable timeTable = overTime.getTimeTable();
                assertThat(overTime).as("연장 근무 정보").isNotNull();
                assertThat(overTime.getPay()).as("연장 근무 수당").isEqualTo(24000);
                assertThat(timeTable.getStartAt()).as("연장 근무 시작 시간").isEqualTo(LocalDateTime.of(today, LocalTime.of(18, 0)));
                assertThat(timeTable.getEndAt()).as("연장 근무 종료 시간").isEqualTo(LocalDateTime.of(today, LocalTime.of(22, 0)));
                assertThat(timeTable.getWorkDuration()).as("연장 근무 시간").isEqualTo(WorkDuration.of(4, 0));
            }
        );
    }

    @Test
    @DisplayName("야간 근무가 포함된 출결의 급여 산출")
    public void attendance_containsNightShift() {
        // Given
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(21, 5));
        final LocalDateTime endAt = LocalDateTime.of(nextDay, LocalTime.of(5, 15));

        // When
        Attendance given = Attendance.of(startAt, endAt);

        // Then
        ExtraWorks extraWorks = given.getExtraWorks();
        List<ExtraWork> nightShifts = extraWorks.getNightShifts();
        assertAll(
            () -> {
                assertThat(given.getTotalPay()).as("급여 총액").isEqualTo(145250);
                assertThat(given.getBasicPay()).as("기본급").isEqualTo(80000);
                assertThat(given.getWorkDuration()).as("총 근로 시간").isEqualTo(WorkDuration.of(8, 10));
            },
            () -> {
                assertThat(extraWorks.getTotalExtraPay()).as("총 추가 근무 수당").isEqualTo(65250);
                assertThat(extraWorks.getSize()).as("추가 근무 건수").isEqualTo(1);
                assertThat(extraWorks.getExtraWorkTypes()).as("추가 근무 타입").containsOnly(ExtraWorkType.NIGHT_SHIFT, ExtraWorkType.NIGHT_SHIFT);
                assertThat(extraWorks.getOverTime()).as("연장 근로 정보").isEqualTo(null);
                assertThat(extraWorks.getNightShiftDuration()).as("전체 야간 근로 시간").isEqualTo(WorkDuration.of(7, 15));
            },
            () -> {
                ExtraWork nightShift = nightShifts.get(0);
                TimeTable timeTable = nightShift.getTimeTable();
                assertThat(nightShifts).hasSize(1);
                assertThat(nightShift.getPay()).as("첫번째 야간 근무 종료 수당").isEqualTo(65250);
                assertThat(nightShift.getWorkDuration()).as("첫번째 야간 근로 시간").isEqualTo(WorkDuration.of(7, 15));
                assertThat(timeTable.getStartAt()).as("첫번째 야간 근무 구간 시작 시간").isEqualTo(LocalDateTime.of(today, LocalTime.of(22, 0)));
                assertThat(timeTable.getEndAt()).as("첫번째 야간 근무 종료 시간").isEqualTo(LocalDateTime.of(nextDay, LocalTime.of(5, 15)));
            }
        );
    }

    @Test
    @DisplayName("연장/야간 근무가 포함된 출결의 급여 산출")
    public void attendance_containsOvertimeAndNightShift() {
        // Given
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(5, 30));
        final LocalDateTime endAt = LocalDateTime.of(nextDay, LocalTime.of(9, 15));

        // When
        Attendance given = Attendance.of(startAt, endAt);

        // Then
        ExtraWorks extraWorks = given.getExtraWorks();
        ExtraWork overTime = extraWorks.getOverTime();
        List<ExtraWork> nightShifts = extraWorks.getNightShifts();

        assertAll(
            () -> {
                assertThat(given.getTotalPay()).as("급여 총액").isEqualTo(459000);
                assertThat(given.getBasicPay()).as("기본급").isEqualTo(270000);
                assertThat(given.getWorkDuration()).as("총 근로 시간").isEqualTo(WorkDuration.of(27, 45));
            },
            () -> {
                assertThat(extraWorks.getTotalExtraPay()).as("총 추가 근무 수당").isEqualTo(189000);
                assertThat(extraWorks.getSize()).as("추가 근무 건수").isEqualTo(3);
                assertThat(extraWorks.getExtraWorkTypes()).as("추가 근무 타입").containsOnly(ExtraWorkType.OVERTIME, ExtraWorkType.NIGHT_SHIFT);
                assertThat(extraWorks.getTotalNightShiftPay()).as("전체 야간 근로 수당").isEqualTo(76500);
                assertThat(extraWorks.getNightShiftDuration()).as("전체 야간 근로 시간").isEqualTo(WorkDuration.of(8, 30));
            },
            () -> {
                assertThat(overTime).as("연장 근무 정보").isNotNull();
                assertThat(overTime.getTimeTable().getStartAt()).as("연장 근무 시작 시간").isEqualTo(LocalDateTime.of(today, LocalTime.of(14, 30)));
                assertThat(overTime.getTimeTable().getEndAt()).as("연장 근무 종료 시간").isEqualTo(LocalDateTime.of(nextDay, LocalTime.of(9, 15)));
                assertThat(overTime.getWorkDuration()).as("연장 근무 시간").isEqualTo(WorkDuration.of(18, 45));
                assertThat(overTime.getPay()).as("연장 근무 수당").isEqualTo(112500);
            },
            () -> {
                ExtraWork firstSectionOfNightShift = nightShifts.get(0);
                ExtraWork secondSectionOfNightShift = nightShifts.get(1);
                assertThat(nightShifts).size().isEqualTo(2);
                assertThat(firstSectionOfNightShift.getTimeTable().getStartAt()).as("첫번째 야간 근무 구간 시작 시간")
                    .isEqualTo(LocalDateTime.of(today, LocalTime.of(5, 30)));
                assertThat(firstSectionOfNightShift.getTimeTable().getEndAt()).as("첫번째 야간 근무 종료 시간")
                    .isEqualTo(LocalDateTime.of(today, LocalTime.of(6, 0)));
                assertThat(firstSectionOfNightShift.getPay()).as("첫번째 야간 근무 종료 수당").isEqualTo(4500);
                assertThat(secondSectionOfNightShift.getTimeTable().getStartAt()).as("두번째 야간 근로 시작 시간")
                    .isEqualTo(LocalDateTime.of(today, LocalTime.of(22, 0)));
                assertThat(secondSectionOfNightShift.getTimeTable().getEndAt()).as("두번째 야간 근로 종료 시간")
                    .isEqualTo(LocalDateTime.of(nextDay, LocalTime.of(6, 0)));
                assertThat(secondSectionOfNightShift.getPay()).as("두번째 야간 근무 수당").isEqualTo(72000);
            }
        );
    }
}
