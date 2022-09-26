package io.sample.attendance.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Domain:Overtime")
class OvertimeTest {
    @ParameterizedTest(name = "[Case#{index}] {0} : 출/퇴근: {1} ~ {2}, 연장 근무 : {3} ~ {4}, 총 연장 근무 : {5}, 연장 근무 수당 : {6}")
    @MethodSource
    @DisplayName("일일 출근 기록으로 부터 연장 근무 시간을 추출")
    public void getOvertime(
        final String testDescription,
        final LocalTime startTime,
        final LocalTime endTime,
        final LocalTime expectedOvertimeStartTime,
        final LocalTime expectedOvertimeEndTime,
        final LocalTime expectedOvertimeDuration,
        final int expectedExtraPay
    ) {
        // Given
        Attendance attendance = Attendance.of(startTime, endTime);

        // When
        Overtime given = Overtime.of(attendance.getStartAt(), attendance.getEndAt());

        // Then
        assertAll(
            () -> assertThat(given.getStartTime()).isEqualTo(expectedOvertimeStartTime),
            () -> assertThat(given.getEndTime()).isEqualTo(expectedOvertimeEndTime),
            () -> assertThat(given.getDuration()).isEqualTo(expectedOvertimeDuration),
            () -> assertThat(given.getExtraPay()).isEqualTo(expectedExtraPay)
        );
    }

    private static Stream<Arguments> getOvertime() {
        return Stream.of(
            Arguments.of(
                "전체 근무 시간이 9시간 이하인 경우",
                LocalTime.of(22, 0),
                LocalTime.of(7, 0),
                LocalTime.of(0, 0),
                LocalTime.of(0, 0),
                LocalTime.of(0, 0),
                0
            ),
            Arguments.of(
                "전체 근무 시간이 9시간 초과인 경우",
                LocalTime.of(22, 0),
                LocalTime.of(7, 30),
                LocalTime.of(7, 0),
                LocalTime.of(7, 30),
                LocalTime.of(0, 30),
                3000
            )
        );
    }
}
