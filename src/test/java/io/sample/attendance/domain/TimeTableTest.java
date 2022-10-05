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

@DisplayName("Domain:TimeTable")
class TimeTableTest {
    @ParameterizedTest(name = "[Case#{index}]{0} : {1} ~ {2}")
    @MethodSource
    @DisplayName("[예외] 올바르지 않은 시간 구간 객체 생성")
    public void throwException_WhenInvalidTimeTableArguments(
        final String description,
        final LocalDateTime startAt,
        final LocalDateTime endAt
    ) {
        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .as(description)
            .isThrownBy(() -> TimeTable.of(startAt, endAt));
    }

    private static Stream<Arguments> throwException_WhenInvalidTimeTableArguments() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
            Arguments.of("시작 시간과 종료 시간이 null인 경우", null, null),
            Arguments.of("종료 시간이 null인 경우", now, null),
            Arguments.of("시작 시간이 null인 경우", null, now),
            Arguments.of("시작 시간과 종료 시간이 같은 경우", now, now),
            Arguments.of("종료 시간이 시작 시간보다 빠른 경우", now, now.minusSeconds(1))
        );
    }

    @Test
    @DisplayName("시작/종료 시간을 표현하는 시간 구간 객체 생성")
    public void createTimeTable() {
        // Given
        final LocalDate today = LocalDate.now();
        final LocalDate nextDay = today.plusDays(1);
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(21, 25));
        final LocalDateTime endAt = LocalDateTime.of(nextDay, LocalTime.of(5, 1));

        // When
        TimeTable given = TimeTable.of(startAt, endAt);

        // Then
        assertAll(
            () -> assertThat(given.getWorkDuration())
                .as("시작/종료 사이 소요 시간을 hh:mm 형식으로 반환")
                .isEqualTo(WorkDuration.of(7, 36)),
            () -> assertThat(given.getWorkDurationByMinute())
                .as("시작/종료 사이 소요 시간을 분으로 환산")
                .isEqualTo(456)
        );
    }
}
