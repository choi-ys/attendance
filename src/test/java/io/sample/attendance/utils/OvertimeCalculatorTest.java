package io.sample.attendance.utils;

import static org.assertj.core.api.Assertions.assertThat;

import io.sample.attendance.domain.Attendance;
import io.sample.attendance.domain.ExtraWork;
import io.sample.attendance.domain.TimeTable;
import io.sample.attendance.domain.WorkDuration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Utils:OvertimeCalculator")
class OvertimeCalculatorTest {
    @ParameterizedTest
    @MethodSource
    @DisplayName("연장 근무 여부 판별")
    public void isOvertime(
        final LocalDateTime startAt,
        final LocalDateTime endAt,
        final boolean expected
    ) {
        // When & Then
        Attendance given = Attendance.of(startAt, endAt);
        assertThat(OvertimeCalculator.isOverTime(given)).isEqualTo(expected);
    }

    private static Stream<Arguments> isOvertime() {
        final LocalDate today = LocalDate.now();
        return Stream.of(
            Arguments.of(
                LocalDateTime.of(today, LocalTime.of(9, 0)),
                LocalDateTime.of(today, LocalTime.of(18, 0)),
                false
            ),
            Arguments.of(
                LocalDateTime.of(today, LocalTime.of(9, 0)),
                LocalDateTime.of(today, LocalTime.of(18, 1)),
                true
            )
        );
    }

    @Test
    @DisplayName("연장 근무 정보 추출")
    public void extract() {
        // Given
        final LocalDate today = LocalDate.now();
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(9, 0));
        final LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(18, 1));

        Attendance attendance = Attendance.of(startAt, endAt);
        ExtraWork given = OvertimeCalculator.extract(attendance);

        // When & Then
        assertThat(given.getPay()).isEqualTo(100);
        assertThat(given.isOvertime()).isTrue();
        assertThat(given.getTimeTable()).isEqualTo(TimeTable.of(endAt.minusMinutes(1), endAt));
        assertThat(given.getWorkDuration()).isEqualTo(WorkDuration.of(0, 1));
    }
}
