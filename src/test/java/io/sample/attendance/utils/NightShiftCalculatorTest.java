package io.sample.attendance.utils;

import static io.sample.attendance.generator.fixture.TestCaseArgumentsGenerator.add;
import static org.assertj.core.api.Assertions.assertThat;

import io.sample.attendance.domain.Attendance;
import io.sample.attendance.domain.ExtraWork;
import io.sample.attendance.domain.ExtraWorkType;
import io.sample.attendance.generator.fixture.TestCaseArgumentsGenerator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Utils:NightShiftCalculator")
class NightShiftCalculatorTest {
    @ParameterizedTest(name = "[Case#{index}]{0} : {1} ~ {2}")
    @MethodSource(value = "isNightShift")
    @DisplayName("야간 근무 여부 판별")
    public void isNightShift(
        final String description,
        final LocalDateTime startAt,
        final LocalDateTime endAt,
        final boolean expected
    ) {
        // When & Then
        Attendance given = Attendance.of(startAt, endAt);
        assertThat(NightShiftCalculator.isNightShift(given)).isEqualTo(expected);
    }

    private static Stream<Arguments> isNightShift() {
        return Stream.of(
            Arguments.of(add(TestCaseArgumentsGenerator::notNightShift, false)),
            Arguments.of(add(TestCaseArgumentsGenerator::oneNightShiftSectionAndOneWorkDay, true)),
            Arguments.of(add(TestCaseArgumentsGenerator::oneNightShiftSectionAndTwoWorkDay, true)),
            Arguments.of(add(TestCaseArgumentsGenerator::twoNightShiftSectionAndOneWorkDay, true)),
            Arguments.of(add(TestCaseArgumentsGenerator::twoNightShiftSectionAndTwoWorkDay, true)),
            Arguments.of(add(TestCaseArgumentsGenerator::oneNightShiftSectionAndTwoWorkDayAndMaximumWork, true)),
            Arguments.of(add(TestCaseArgumentsGenerator::twoNightShiftSectionAndThreeWorkDayAndMaximumWork, true))
        );
    }

    @ParameterizedTest(name = "[Case#{index}]{0} : {1} ~ {2}, {3}")
    @MethodSource
    @DisplayName("야간 근무 구간 추출")
    public void extractNightShiftSections(
        final String description,
        final LocalDateTime startAt,
        final LocalDateTime endAt,
        final List<ExtraWork> expected
    ) {
        // When
        Attendance given = Attendance.of(startAt, endAt);
        List<ExtraWork> extract = NightShiftCalculator.extract(given);

        // Then
        assertThat(extract).containsExactlyElementsOf(expected);
    }

    private static Stream<Arguments> extractNightShiftSections() {
        LocalDate today = LocalDate.now();
        LocalDate nextDay = today.plusDays(1);
        LocalDate afterTwoDays = today.plusDays(2);
        return Stream.of(
            Arguments.of(
                add(TestCaseArgumentsGenerator::oneNightShiftSectionAndOneWorkDay,
                    Collections.singletonList(
                        ExtraWork.of(
                            null,
                            LocalDateTime.of(today, LocalTime.of(22, 0)),
                            LocalDateTime.of(today, LocalTime.of(22, 1)),
                            ExtraWorkType.NIGHT_SHIFT
                        )
                    )
                )
            ),
            Arguments.of(
                add(TestCaseArgumentsGenerator::oneNightShiftSectionAndTwoWorkDay,
                    Collections.singletonList(
                        ExtraWork.of(
                            null,
                            LocalDateTime.of(today, LocalTime.of(22, 0)),
                            LocalDateTime.of(nextDay, LocalTime.of(6, 0)),
                            ExtraWorkType.NIGHT_SHIFT
                        )
                    )
                )
            ),
            Arguments.of(
                add(TestCaseArgumentsGenerator::twoNightShiftSectionAndOneWorkDay,
                    Arrays.asList(
                        ExtraWork.of(
                            null,
                            LocalDateTime.of(today, LocalTime.of(5, 59)),
                            LocalDateTime.of(today, LocalTime.of(6, 0)),
                            ExtraWorkType.NIGHT_SHIFT
                        ),
                        ExtraWork.of(
                            null,
                            LocalDateTime.of(today, LocalTime.of(22, 0)),
                            LocalDateTime.of(today, LocalTime.of(22, 1)),
                            ExtraWorkType.NIGHT_SHIFT
                        )
                    )
                )
            ),
            Arguments.of(
                add(TestCaseArgumentsGenerator::twoNightShiftSectionAndTwoWorkDay,
                    Arrays.asList(
                        ExtraWork.of(
                            null,
                            LocalDateTime.of(today, LocalTime.of(22, 0)),
                            LocalDateTime.of(nextDay, LocalTime.of(6, 0)),
                            ExtraWorkType.NIGHT_SHIFT
                        ),
                        ExtraWork.of(
                            null,
                            LocalDateTime.of(nextDay, LocalTime.of(22, 0)),
                            LocalDateTime.of(nextDay, LocalTime.of(22, 1)),
                            ExtraWorkType.NIGHT_SHIFT
                        )
                    )
                )
            ),
            Arguments.of(
                add(TestCaseArgumentsGenerator::oneNightShiftSectionAndTwoWorkDayAndMaximumWork,
                    Collections.singletonList(
                        ExtraWork.of(
                            null,
                            LocalDateTime.of(today, LocalTime.of(22, 0)),
                            LocalDateTime.of(nextDay, LocalTime.of(6, 0)),
                            ExtraWorkType.NIGHT_SHIFT
                        )
                    )
                )
            ),
            Arguments.of(
                add(TestCaseArgumentsGenerator::twoNightShiftSectionAndTwoWorkDayAndMaximumWork,
                    Arrays.asList(
                        ExtraWork.of(
                            null,
                            LocalDateTime.of(today, LocalTime.of(22, 0)),
                            LocalDateTime.of(nextDay, LocalTime.of(6, 0)),
                            ExtraWorkType.NIGHT_SHIFT
                        ),
                        ExtraWork.of(
                            null,
                            LocalDateTime.of(nextDay, LocalTime.of(22, 0)),
                            LocalDateTime.of(nextDay, LocalTime.of(23, 59)),
                            ExtraWorkType.NIGHT_SHIFT
                        )
                    )
                )
            ),
            Arguments.of(
                add(TestCaseArgumentsGenerator::twoNightShiftSectionAndThreeWorkDayAndMaximumWork,
                    Arrays.asList(
                        ExtraWork.of(
                            null,
                            LocalDateTime.of(today, LocalTime.of(22, 0)),
                            LocalDateTime.of(nextDay, LocalTime.of(6, 0)),
                            ExtraWorkType.NIGHT_SHIFT
                        ),
                        ExtraWork.of(
                            null,
                            LocalDateTime.of(nextDay, LocalTime.of(22, 0)),
                            LocalDateTime.of(afterTwoDays, LocalTime.of(1, 59)),
                            ExtraWorkType.NIGHT_SHIFT
                        )
                    )
                )
            )
        );
    }
}
