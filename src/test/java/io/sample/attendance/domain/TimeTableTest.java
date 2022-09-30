package io.sample.attendance.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Domain:TimeTable")
class TimeTableTest {
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
            () -> assertThat(given.getDuration())
                .as("시작/종료 사이 소요 시간을 hh:mm 형식으로 반환")
                .isEqualTo(WorkDuration.of(7, 36)),
            () -> assertThat(given.getDurationByMinute())
                .as("시작/종료 사이 소요 시간을 분으로 환산")
                .isEqualTo(456)
        );
    }
}
