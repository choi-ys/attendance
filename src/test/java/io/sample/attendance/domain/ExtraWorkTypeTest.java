package io.sample.attendance.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Domain:ExtraWorkType")
class ExtraWorkTypeTest {
    @ParameterizedTest(name = "[Case#{index}]{0} : 추가 근무 타입 : {1}, 추가 수당 : {2}")
    @MethodSource
    @DisplayName("추가 근무 타입에 따른 추가 수당 계산")
    public void calculateExtraPay(
        final String description,
        final ExtraWorkType given,
        final int ExpectedExtraPayPerMinute
    ) {
        // Given
        int totalExtraWorkMinutes = 10;

        // When & Then
        assertThat(given.calculateExtraPay(totalExtraWorkMinutes))
            .as("분급으로 계산한 추가 수당의 총액")
            .isEqualTo(ExpectedExtraPayPerMinute);
    }

    private static Stream<Arguments> calculateExtraPay() {
        return Stream.of(
            Arguments.of(ExtraWorkType.NIGHT_SHIFT.getDescription(), ExtraWorkType.NIGHT_SHIFT, 1500),
            Arguments.of(ExtraWorkType.OVERTIME.getDescription(), ExtraWorkType.OVERTIME, 1000)
        );
    }
}
