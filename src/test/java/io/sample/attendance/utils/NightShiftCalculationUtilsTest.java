package io.sample.attendance.utils;

import static io.sample.attendance.fixture.TestCaseArgumentsGenerator.addArguments;
import static io.sample.attendance.fixture.TestCaseArgumentsGenerator.continuousTestCaseArguments;
import static io.sample.attendance.fixture.TestCaseArgumentsGenerator.notContinuousTestCaseArguments;
import static io.sample.attendance.fixture.TestCaseArgumentsGenerator.sameDayWorkTestCaseArguments;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.type.IntegerType.ZERO;

import io.sample.attendance.fixture.TestCaseArgumentsGenerator;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Utils:NightShiftCalculation")
class NightShiftCalculationUtilsTest {
    @ParameterizedTest(name = "[Case#{index}] {0} : {1} ~ {2}, 당일 퇴근 여부 : {3}")
    @MethodSource
    @DisplayName("당일 퇴근 여부 판별")
    void isSameWorkDay(
        final String description,
        final LocalDateTime startAt,
        final LocalDateTime endAt,
        final boolean expected
    ) {
        // When & Then
        assertThat(NightShiftCalculationUtils.isSameWorkDay(startAt, endAt)).isEqualTo(expected);
    }

    private static Stream<Arguments> isSameWorkDay() {
        return Stream.of(
            Arguments.of(addArguments(TestCaseArgumentsGenerator::sameDayWorkTestCaseArguments, false)),
            Arguments.of(addArguments(TestCaseArgumentsGenerator::continuousTestCaseArguments, true)),
            Arguments.of(addArguments(TestCaseArgumentsGenerator::notContinuousTestCaseArguments, true))
        );
    }

    @ParameterizedTest(name = "[Case#{index}] {0} : {1} ~ {2} : 연속된 야간 근무 여부 : {3}")
    @MethodSource
    @DisplayName("연속된 야간 근무 여부 판별")
    void isContinuous(
        final String description,
        final LocalDateTime startAt,
        final LocalDateTime endAt,
        final boolean expected
    ) {
        // When & Then
        assertThat(NightShiftCalculationUtils.isContinuous(startAt, endAt)).isEqualTo(expected);
    }

    private static Stream<Arguments> isContinuous() {
        return Stream.of(
            Arguments.of(addArguments(TestCaseArgumentsGenerator::continuousTestCaseArguments, true)),
            Arguments.of(addArguments(TestCaseArgumentsGenerator::notContinuousTestCaseArguments, false))
        );
    }

    @ParameterizedTest(name = "[Case#{index}] {0} : {1} ~ {2} : 연속되지 않은 야간 근무 여부 : {3}")
    @MethodSource
    @DisplayName("연속되지 않은 야간 근무 여부 판별")
    void isNotContinuous(
        final String description,
        final LocalDateTime startAt,
        final LocalDateTime endAt,
        final boolean expected
    ) {
        // When & Then
        assertThat(NightShiftCalculationUtils.isNotContinuous(startAt, endAt)).isEqualTo(expected);
    }

    private static Stream<Arguments> isNotContinuous() {
        return Stream.of(
            Arguments.of(addArguments(TestCaseArgumentsGenerator::sameDayWorkTestCaseArguments, false)),
            Arguments.of(addArguments(TestCaseArgumentsGenerator::continuousTestCaseArguments, false)),
            Arguments.of(addArguments(TestCaseArgumentsGenerator::notContinuousTestCaseArguments, true))
        );
    }

    @ParameterizedTest(name = "[Case#{index}] : {0}, {1}")
    @MethodSource
    @DisplayName("야간 근무 산정 범위 포함 여부 판별 : 22:00 ~ 익일 06:00의 경계값")
    void isIncludeInNightShiftTimeRange(
        final LocalTime given,
        final boolean expected
    ) {
        // When
        assertThat(NightShiftCalculationUtils.isIncludeInNightShiftTimeRange(given)).isEqualTo(expected);
    }

    private static Stream<Arguments> isIncludeInNightShiftTimeRange() {
        return Stream.of(
            Arguments.of(LocalTime.of(21, 59), false),
            Arguments.of(LocalTime.of(22, ZERO), false),
            Arguments.of(LocalTime.of(22, 1), true),
            Arguments.of(LocalTime.of(5, 59), true),
            Arguments.of(LocalTime.of(6, ZERO), false),
            Arguments.of(LocalTime.of(6, 1), false)
        );
    }

    @ParameterizedTest(name = "[Case#{index}] {0} : {1} ~ {2}")
    @MethodSource
    @DisplayName("야간 근무 여부 판별")
    void isNightShift(
        final String description,
        final LocalDateTime startAt,
        final LocalDateTime endAt
    ) {
        // When & Then
        assertThat(NightShiftCalculationUtils.isNightShift(startAt, endAt)).isTrue();
    }

    private static Stream<Arguments> isNightShift() {
        return Stream.of(
            Arguments.of(sameDayWorkTestCaseArguments()),
            Arguments.of(continuousTestCaseArguments()),
            Arguments.of(notContinuousTestCaseArguments())
        );
    }
}
