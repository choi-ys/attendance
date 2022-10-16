package io.sample.attendance.model.domain;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Domain:WorkDuration")
class WorkDurationTest {
    @Test
    @DisplayName("[예외]28시간 이상 근무 하는 경우")
    public void expressDuration() {
        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> WorkDuration.of(28, 1));
    }
}
