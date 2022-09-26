package io.sample.attendance.domain;

import static io.sample.attendance.domain.NightShift.NIGHT_SHIFT_END_TIME;
import static io.sample.attendance.domain.NightShift.NIGHT_SHIFT_START_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Domain:NightShift")
class NightShiftTest {
    @ParameterizedTest(name = "[Case#{index}] : 출/퇴근: {0} ~ {1}, 연장 근무 : {2} ~ {3}, 총 야간 근무 : {4}, 연장 야간 수당 : {5}")
    @MethodSource
    @DisplayName("일일 출근 기록으로 부터 야간 근무 시간을 추출")
    public void getNightShift(
        final LocalTime startTime,
        final LocalTime endTime,
        final LocalTime expectedNightShiftStartTime,
        final LocalTime expectedNightShiftEndTime,
        final LocalTime expectedOvertimeDuration,
        final int expectedExtraPay
    ) {
        // Given
        Attendance attendance = Attendance.of(startTime, endTime);

        // When
        NightShift given = NightShift.of(attendance.getStartAt(), attendance.getEndAt());

        // Then
        assertAll(
            () -> assertThat(given.getStartTime()).isEqualTo(expectedNightShiftStartTime),
            () -> assertThat(given.getEndTime()).isEqualTo(expectedNightShiftEndTime),
            () -> assertThat(given.getDuration()).isEqualTo(expectedOvertimeDuration),
            () -> assertThat(given.getExtraPay()).isEqualTo(expectedExtraPay)
        );
    }

    private static Stream<Arguments> getNightShift() {
        return Stream.of(
            Arguments.of(
                LocalTime.of(9, 0), LocalTime.of(18, 0),
                LocalTime.MIDNIGHT, LocalTime.MIDNIGHT,
                LocalTime.MIDNIGHT, 0
            ),
            Arguments.of(
                LocalTime.of(21, 0), LocalTime.of(5, 0),
                NIGHT_SHIFT_START_TIME, LocalTime.of(5, 0),
                LocalTime.of(7, 0), 63000
            ),
            Arguments.of(
                LocalTime.of(21, 0), LocalTime.of(7, 0),
                NIGHT_SHIFT_START_TIME, NIGHT_SHIFT_END_TIME,
                LocalTime.of(8, 0), 72000
            ),
            Arguments.of(
                LocalTime.of(23, 0), LocalTime.of(5, 0),
                LocalTime.of(23, 0), LocalTime.of(5, 0),
                LocalTime.of(6, 0), 54000
            ),
            Arguments.of(
                LocalTime.of(23, 0), LocalTime.of(7, 0),
                LocalTime.of(23, 0), NIGHT_SHIFT_END_TIME,
                LocalTime.of(7, 0), 63000
            ),
            Arguments.of(
                LocalTime.MIDNIGHT, LocalTime.of(5, 0),
                LocalTime.MIDNIGHT, LocalTime.of(5, 0),
                LocalTime.of(5, 0), 45000
            ),
            Arguments.of(
                LocalTime.MIDNIGHT, LocalTime.of(7, 0),
                LocalTime.MIDNIGHT, NIGHT_SHIFT_END_TIME,
                LocalTime.of(6, 0), 54000
            ),
            Arguments.of(
                LocalTime.of(1, 0), LocalTime.of(5, 0),
                LocalTime.of(1, 0), LocalTime.of(5, 0),
                LocalTime.of(4, 0), 36000
            ),
            Arguments.of(
                LocalTime.of(1, 0), LocalTime.of(7, 0),
                LocalTime.of(1, 0), NIGHT_SHIFT_END_TIME,
                LocalTime.of(5, 0), 45000
            )
        );
    }
}
