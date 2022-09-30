package io.sample.attendance.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Domain:ExtraWorks")
class ExtraWorksTest {
    final LocalDate today = LocalDate.now();
    final LocalDate nextDay = today.plusDays(1);
    final LocalTime startTime = LocalTime.of(22, 0);
    final LocalTime endTime = LocalTime.of(6, 0);
    final LocalDateTime startAt = LocalDateTime.of(today, startTime);
    final LocalDateTime endAt = LocalDateTime.of(nextDay, endTime);

    ExtraWork nightShift = ExtraWork.of(startAt, endAt, ExtraWorkType.NIGHT_SHIFT);
    ExtraWork overtime = ExtraWork.of(startAt, endAt, ExtraWorkType.OVERTIME);

    @Test
    @DisplayName("추가 근무 목록 객체 생성")
    public void create() {
        // Given
        ExtraWorks extraWorks = new ExtraWorks();

        // When
        extraWorks.add(nightShift);
        extraWorks.add(overtime);

        // Then
        assertAll(
            () -> assertThat(extraWorks.getSize())
                .as("추가 근무 건수")
                .isEqualTo(2),
            () -> assertThat(extraWorks.getTotalExtraPay())
                .as("추가 수당의 합")
                .isEqualTo(120000),
            () -> assertThat(extraWorks.getExtraWorkTypes())
                .as("추가 근무 타입 목록")
                .contains(ExtraWorkType.NIGHT_SHIFT, ExtraWorkType.OVERTIME)
        );
    }
}
