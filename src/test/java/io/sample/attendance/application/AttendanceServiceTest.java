package io.sample.attendance.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.sample.attendance.domain.Attendance;
import io.sample.attendance.dto.AttendanceDto;
import io.sample.attendance.dto.AttendanceDto.AttendanceRequest;
import io.sample.attendance.dto.AttendanceDto.AttendanceResponse;
import io.sample.attendance.fixture.AttendanceFixtureGenerator;
import io.sample.attendance.repo.AttendanceRepo;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:Attendance")
public class AttendanceServiceTest {
    @Mock
    private AttendanceRepo attendanceRepo;

    @InjectMocks
    private AttendanceService attendanceService;

    static Attendance 추가_근무가_없는_근무, 연장근무가_포함된_근무, 야간근무가_포함된_근무, 연장근무와_야간근무가_포함된_근무;
    static AttendanceRequest 추가_근무가_없는_근무_생성_요청, 연장근무가_포함된_근무_생성_요청, 야간근무가_포함된_근무_생성_요청, 연장근무와_야간근무가_포함된_근무_생성_요청;

    @BeforeAll
    static void setUp() {
        추가_근무가_없는_근무 = AttendanceFixtureGenerator.추가_근무가_없는_근무();
        연장근무가_포함된_근무 = AttendanceFixtureGenerator.연장근무가_포함된_근무();
        야간근무가_포함된_근무 = AttendanceFixtureGenerator.야간근무가_포함된_근무();
        연장근무와_야간근무가_포함된_근무 = AttendanceFixtureGenerator.연장근무와_야간근무가_포함된_근무();

        추가_근무가_없는_근무_생성_요청 = AttendanceDto.AttendanceRequest.of(추가_근무가_없는_근무.getStartAt(), 추가_근무가_없는_근무.getEndAt());
        연장근무가_포함된_근무_생성_요청 = AttendanceDto.AttendanceRequest.of(연장근무가_포함된_근무.getStartAt(), 연장근무가_포함된_근무.getEndAt());
        야간근무가_포함된_근무_생성_요청 = AttendanceDto.AttendanceRequest.of(야간근무가_포함된_근무.getStartAt(), 야간근무가_포함된_근무.getEndAt());
        연장근무와_야간근무가_포함된_근무_생성_요청 = AttendanceDto.AttendanceRequest.of(연장근무와_야간근무가_포함된_근무.getStartAt(), 연장근무와_야간근무가_포함된_근무.getEndAt());
    }

    @ParameterizedTest(name = "[Case#{index}]{0}")
    @MethodSource
    @DisplayName("일일 근태 기록 생성")
    public void saveAttendance(
        final String description,
        final Attendance 근무,
        final AttendanceRequest 근무_생성_요청
    ) {
        // Given
        근무_생성_제어(근무);

        // When
        AttendanceResponse 근무_생성_응답 = attendanceService.saveAttendance(근무_생성_요청);

        // Then
        근무_생성_검증(근무, 근무_생성_응답);
    }

    private static Stream<Arguments> saveAttendance() {
        return Stream.of(
            Arguments.of("추가_근무가_없는_근무_생성_요청", 추가_근무가_없는_근무, 추가_근무가_없는_근무_생성_요청),
            Arguments.of("연장근무가_포함된_근무_생성_요청", 연장근무가_포함된_근무, 연장근무가_포함된_근무_생성_요청),
            Arguments.of("야간근무가_포함된_근무_생성_요청", 야간근무가_포함된_근무, 야간근무가_포함된_근무_생성_요청),
            Arguments.of("연장근무와_야간근무가_포함된_근무_생성_요청", 연장근무와_야간근무가_포함된_근무, 연장근무와_야간근무가_포함된_근무_생성_요청)
        );
    }

    private void 근무_생성_제어(Attendance attendance) {
        given(attendanceRepo.save(attendance)).will(AdditionalAnswers.returnsFirstArg());
    }

    private void 근무_생성_검증(Attendance 근무, AttendanceResponse 정규_근무_생성_응답) {
        assertAll(
            () -> assertThat(정규_근무_생성_응답.getId()).isEqualTo(근무.getId()),
            () -> assertThat(정규_근무_생성_응답.getStartAt()).isEqualTo(근무.getStartAt()),
            () -> assertThat(정규_근무_생성_응답.getEndAt()).isEqualTo(근무.getEndAt()),
            () -> assertThat(정규_근무_생성_응답.getBasicPay()).isEqualTo(근무.getBasicPay()),
            () -> assertThat(정규_근무_생성_응답.getTotalPay()).isEqualTo(근무.getTotalPay())
        );
        verify(attendanceRepo).save(근무);
    }
}
