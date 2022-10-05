package io.sample.attendance.domain;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Domain:Attendance")
class AttendanceTest {
    private final LocalDate today = LocalDate.now();

    @ParameterizedTest(name = "[Case#{index}]{0} : {1} ~ {2}")
    @MethodSource
    @DisplayName("[예외] 출근 시간과 퇴근 시간이 옳바르지 않은 경우")
    public void throwException_WhenStartAtAndEndAtIsInValid(
        final String description,
        final LocalDateTime startAt,
        final LocalDateTime endAt
    ) {
        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
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
}
