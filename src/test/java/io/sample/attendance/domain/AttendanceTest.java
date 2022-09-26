package io.sample.attendance.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

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
    @ParameterizedTest(name = "[Case#{index}] {0} : 근무시간 = {1} ~ {2}, 퇴근일 = {3}")
    @MethodSource
    @DisplayName("근무 시작/종료 시간을 이용한 근태 객체 생성")
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
            () -> assertThat(given.getStartAt()).isEqualTo(LocalDateTime.of(expectedDate, startTime)),
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
}
