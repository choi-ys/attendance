package io.sample.attendance.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Domain:ExtraWork")
class ExtraWorkTest {
    final LocalDate today = LocalDate.now();
    final LocalDate nextDay = today.plusDays(1);
    final LocalTime startTime = LocalTime.of(22, 0);
    final LocalTime endTime = LocalTime.of(6, 0);
    final LocalDateTime startAt = LocalDateTime.of(today, startTime);
    final LocalDateTime endAt = LocalDateTime.of(nextDay, endTime);

    @Test
    @DisplayName("추가 근무 정보 생성 : 야간 근무")
    public void createNightShiftTypeExtraWork() {
        // When
        ExtraWork given = ExtraWork.of(null, startAt, endAt, ExtraWorkType.NIGHT_SHIFT);

        // Then
        assertAll(
            () -> assertThat(given.isNightShift()).as("추가 근무 유형").isTrue(),
            () -> assertThat(given.getWorkDuration()).as("총 작업 시간").isEqualTo(WorkDuration.of(8, 0)),
            () -> assertThat(given.getPay()).as("추가 근로 수당").isEqualTo(72000)
        );
    }

    @Test
    @DisplayName("추가 근무 정보 생성 : 연장 근무")
    public void createOvertimeTypeExtraWork() {
        // When
        ExtraWork extraWork = ExtraWork.of(null, startAt, endAt, ExtraWorkType.OVERTIME);

        // Then
        assertAll(
            () -> assertThat(extraWork.isOvertime()).as("추가 근무 유형").isTrue(),
            () -> assertThat(extraWork.getWorkDuration()).as("총 작업 시간").isEqualTo(WorkDuration.of(8, 0)),
            () -> assertThat(extraWork.getPay()).as("추가 근로 수당").isEqualTo(48000)
        );
    }
}
