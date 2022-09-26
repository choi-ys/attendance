package io.sample.attendance.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.sample.attendance.ui.DailyPayStubView;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Domain:Attendance")
class AttendanceTest {
    @ParameterizedTest(name = "[Case#{index}] {0} : 출/퇴근 시간 : {1} ~ {2}, 퇴근일 : {3}")
    @MethodSource
    @DisplayName("출/퇴근 시간을 이용한 근태 객체 생성")
    public void createAttendance(
        final String testCaseDescription,
        final LocalTime startTime,
        final LocalTime endTime,
        final LocalDate expectedDate
    ) {
        // When
        Attendance given = Attendance.of(startTime, endTime);

        // Then
        assertAll(
            () -> assertThat(given.getStartAt()).isEqualTo(LocalDateTime.of(LocalDate.now(), startTime)),
            () -> assertThat(given.getEndAt()).isEqualTo(LocalDateTime.of(expectedDate, endTime))
        );
    }

    private static Stream<Arguments> createAttendance() {
        final LocalDate today = LocalDate.now();
        return Stream.of(
            Arguments.of("출/퇴근일이 같은 경우", LocalTime.of(10, 0), LocalTime.of(19, 0), today),
            Arguments.of("출/퇴근일이 다른 경우", LocalTime.of(22, 0), LocalTime.of(7, 0), today.plusDays(1))
        );
    }

    @Test
    @DisplayName("[예외] 근무 시작과 종료 시간이 같은 경우")
    public void throwException_WhenStartTimeAndEndTImeIsSame() {
        // Given
        final LocalTime localTime = LocalTime.of(0, 0);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> Attendance.of(localTime, localTime));
    }

    @ParameterizedTest(name = "[Case#{index}] {0} : 출/퇴근 시간 : {1} ~ {2}, 급여 총액 : {3}, 총 근로 시간 : {4}, "
        + "연장 근무 시간 : {5}, {6} ~ {7}, 연장 근로 수당 : {8}, "
        + "야간 근무 시간 : {9}, {10} ~ {11}, 야간 근로 수당 : {12}")
    @MethodSource
    @DisplayName("출/퇴근 시간으로부터 일일 급여 명세서 산출")
    public void dailyPayStub(
        final String testCaseDescription,
        final LocalTime startTime,
        final LocalTime endTime,
        final int expectedTotalPay,
        final LocalTime expectedWorkingTime,
        final LocalTime expectedOvertimeWorkingTime,
        final LocalTime expectedOvertimeStartAt,
        final LocalTime expectedOvertimeEndAt,
        final int expectedOvertimeExtraPay,
        final LocalTime expectedNightShiftWorkingTime,
        final LocalTime expectedNightShiftStartAt,
        final LocalTime expectedNightShiftEndAt,
        final int expectedNightShiftExtraPay
    ) {
        // given
        Attendance given = Attendance.of(startTime, endTime);

        // When & Then
        assertAll(
            () -> assertThat(given.getStartAt().toLocalTime()).as("출근 시간").isEqualTo(startTime),
            () -> assertThat(given.getEndAt().toLocalTime()).as("퇴근 시간").isEqualTo(endTime),
            () -> assertThat(given.getTotalPay()).as("급여 총액").isEqualTo(expectedTotalPay),
            () -> assertThat(given.getWorkingTime()).as("총 근무 시간").isEqualTo(expectedWorkingTime),
            () -> assertThat(given.getOvertimeWorkingTime()).as("연장 근무 시간").isEqualTo(expectedOvertimeWorkingTime),
            () -> assertThat(given.getOvertimeStartAt()).as("연장 근무 시작 시간").isEqualTo(expectedOvertimeStartAt),
            () -> assertThat(given.getOvertimeEndAt()).as("연장 근무 종료 시간").isEqualTo(expectedOvertimeEndAt),
            () -> assertThat(given.getOvertimeExtraPay()).as("연장 근로 수당").isEqualTo(expectedOvertimeExtraPay),
            () -> assertThat(given.getNightShiftWorkingTime()).as("야간 근무 시간").isEqualTo(expectedNightShiftWorkingTime),
            () -> assertThat(given.getNightShiftStartAt()).as("야간 근로 시작 시간").isEqualTo(expectedNightShiftStartAt),
            () -> assertThat(given.getNightShiftEndAt()).as("야간 근로 종료 시간").isEqualTo(expectedNightShiftEndAt),
            () -> assertThat(given.getNightShiftExtraPay()).as("야간 근로 수당").isEqualTo(expectedNightShiftExtraPay)
        );
        DailyPayStubView.printDailyPayStub(given);
    }

    private static Stream<Arguments> dailyPayStub() {
        return Stream.of(
            Arguments.of(
                "연장/야간 근무가 없는 근무",
                LocalTime.of(9, 0), LocalTime.of(18, 0),
                80000, LocalTime.of(8, 0),
                LocalTime.of(0, 0), LocalTime.of(0, 0), LocalTime.of(0, 0), 0,
                LocalTime.of(0, 0), LocalTime.of(0, 0), LocalTime.of(0, 0), 0
            ),
            Arguments.of(
                "연장 근무가 포함된 근무",
                LocalTime.of(9, 0), LocalTime.of(21, 0),
                128000, LocalTime.of(11, 0),
                LocalTime.of(3, 0), LocalTime.of(18, 0), LocalTime.of(21, 0), 18000,
                LocalTime.of(0, 0), LocalTime.of(0, 0), LocalTime.of(0, 0), 0
            ),
            Arguments.of(
                "야간 근무가 포함된 근무",
                LocalTime.of(23, 0), LocalTime.of(4, 0),
                95000, LocalTime.of(5, 0),
                LocalTime.of(0, 0), LocalTime.of(0, 0), LocalTime.of(0, 0), 0,
                LocalTime.of(5, 0), LocalTime.of(23, 0), LocalTime.of(4, 0), 45000
            ),
            Arguments.of(
                "연장/야간 근무가 포함된 근무",
                LocalTime.of(9, 0), LocalTime.of(1, 0),
                219000, LocalTime.of(15, 0),
                LocalTime.of(7, 0), LocalTime.of(18, 0), LocalTime.of(1, 0), 42000,
                LocalTime.of(3, 0), LocalTime.of(22, 0), LocalTime.of(1, 0), 27000
            )
        );
    }
}
