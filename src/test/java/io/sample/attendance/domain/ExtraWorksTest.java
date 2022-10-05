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

    @Test
    @DisplayName("야간/연장 근무가 없는 추가 근무 목록 객체 생성")
    public void create() {
        // Given
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(9, 0));
        final LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(18, 0));
        Attendance attendance = Attendance.of(startAt, endAt);

        // When
        ExtraWorks given = ExtraWorks.from(attendance);

        // Then
        assertAll(
            () -> assertThat(given.getSize()).isZero(),
            () -> assertThat(given.getExtraWorkTypes()).isEmpty(),
            () -> assertThat(given.getTotalExtraPay()).isZero()
        );
    }

    @Test
    @DisplayName("연장 근무가 포함된 추가 근무 목록 객체 생성")
    public void createExtraWorksIncludedOvertime() {
        // Given
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(9, 0));
        final LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(20, 1));
        Attendance attendance = Attendance.of(startAt, endAt);

        // When
        ExtraWorks given = ExtraWorks.from(attendance);

        // Then
        assertAll(
            () -> assertThat(given.getSize()).isEqualTo(1),
            () -> assertThat(given.getExtraWorkTypes()).containsExactly(ExtraWorkType.OVERTIME),
            () -> assertThat(given.getTotalExtraPay()).isEqualTo(12100)
        );
    }

    @Test
    @DisplayName("야간 근무가 포함된 추가 근무 목록 객체 생성")
    public void createExtraWorksIncludedNightShift() {
        // Given
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(22, 0));
        final LocalDateTime endAt = LocalDateTime.of(nextDay, LocalTime.of(6, 0));
        Attendance attendance = Attendance.of(startAt, endAt);

        // When
        ExtraWorks given = ExtraWorks.from(attendance);

        // Then
        assertAll(
            () -> assertThat(given.getSize()).isEqualTo(1),
            () -> assertThat(given.getExtraWorkTypes()).containsExactly(ExtraWorkType.NIGHT_SHIFT),
            () -> assertThat(given.getTotalExtraPay()).isEqualTo(72000)
        );
    }

    @Test
    @DisplayName("연장근무와 야간근무가 모두 포함된 추가 근무 목록 객체 생성")
    public void createExtraWorksIncludedOvertimeAndNightShift() {
        // Given
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(5, 20));
        final LocalDateTime endAt = LocalDateTime.of(nextDay, LocalTime.of(6, 0));
        Attendance attendance = Attendance.of(startAt, endAt);

        // When
        ExtraWorks given = ExtraWorks.from(attendance);

        // Then
        assertAll(
            () -> assertThat(given.getSize()).isEqualTo(3),
            () -> assertThat(given.getExtraWorkTypes()).containsAnyOf(ExtraWorkType.NIGHT_SHIFT, ExtraWorkType.OVERTIME),
            () -> assertThat(given.getTotalExtraPay()).isEqualTo(172000)
        );
    }
}
