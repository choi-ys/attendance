package io.sample.attendance.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.sample.attendance.domain.Attendance;
import io.sample.attendance.domain.ExtraWorkType;
import io.sample.attendance.dto.AttendanceDto;
import io.sample.attendance.dto.AttendanceDto.AttendanceRequest;
import io.sample.attendance.dto.AttendanceDto.AttendanceResponse;
import io.sample.attendance.dto.AttendanceDto.MonthlyAttendanceRequest;
import io.sample.attendance.dto.ExtraWorkResponse;
import io.sample.attendance.generator.fixture.AttendanceFixtureGenerator;
import io.sample.attendance.global.response.PageResponse;
import io.sample.attendance.repo.AttendanceRepo;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

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
        추가_근무가_없는_근무 = AttendanceFixtureGenerator.추가_근무가_없는_출결_생성();
        연장근무가_포함된_근무 = AttendanceFixtureGenerator.연장근무가_포함된_출결_생성();
        야간근무가_포함된_근무 = AttendanceFixtureGenerator.야간근무가_포함된_출결_생성();
        연장근무와_야간근무가_포함된_근무 = AttendanceFixtureGenerator.연장근무와_야간근무가_포함된_출결_생성();

        추가_근무가_없는_근무_생성_요청 = AttendanceDto.AttendanceRequest.of(추가_근무가_없는_근무.getStartAt(), 추가_근무가_없는_근무.getEndAt());
        연장근무가_포함된_근무_생성_요청 = AttendanceDto.AttendanceRequest.of(연장근무가_포함된_근무.getStartAt(), 연장근무가_포함된_근무.getEndAt());
        야간근무가_포함된_근무_생성_요청 = AttendanceDto.AttendanceRequest.of(야간근무가_포함된_근무.getStartAt(), 야간근무가_포함된_근무.getEndAt());
        연장근무와_야간근무가_포함된_근무_생성_요청 = AttendanceDto.AttendanceRequest.of(연장근무와_야간근무가_포함된_근무.getStartAt(), 연장근무와_야간근무가_포함된_근무.getEndAt());
    }

    @ParameterizedTest(name = "[Case#{index}]{0}")
    @MethodSource
    @DisplayName("일일 출결 생성")
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
        근무_응답_항목_검증(근무, 근무_생성_응답);
        verify(attendanceRepo).save(근무);
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

    @Test
    @DisplayName("특정 출결 조회")
    public void findAttendanceById() {
        // Given
        근무_조회_제어(연장근무가_포함된_근무);

        // When
        AttendanceResponse 근무_조회_응답 = attendanceService.findAttendanceResponseById(연장근무가_포함된_근무.getId());

        // Then
        근무_응답_항목_검증(연장근무가_포함된_근무, 근무_조회_응답);
        verify(attendanceRepo).findById(연장근무가_포함된_근무.getId());
    }

    private void 근무_조회_제어(Attendance attendance) {
        given(attendanceRepo.findById(attendance.getId())).willReturn(Optional.of(attendance));
    }

    private void 근무_응답_항목_검증(Attendance 근무, AttendanceResponse 근무_응답) {
        Set<ExtraWorkType> 추가_근무_타입 = 근무.getExtraWorks().getExtraWorkTypes();
        assertAll(
            () -> assertThat(근무_응답.getId()).isEqualTo(근무.getId()),
            () -> assertThat(근무_응답.getStartAt()).isEqualTo(근무.getStartAt()),
            () -> assertThat(근무_응답.getEndAt()).isEqualTo(근무.getEndAt()),
            () -> assertThat(근무_응답.getBasicPay()).isEqualTo(근무.getBasicPay()),
            () -> assertThat(근무_응답.getTotalPay()).isEqualTo(근무.getTotalPay()),
            () -> assertThat(근무_응답.getExtraWorks())
                .extracting(ExtraWorkResponse::getExtraWorkType)
                .containsAnyElementsOf(추가_근무_타입)
        );
    }

    @Test
    @DisplayName("기준년월의 출결 목록 조회")
    public void findMonthlyAttendanceResponsesById() {
        // Given
        final YearMonth 조회월 = YearMonth.from(LocalDate.now());
        final List<Attendance> 기준년월의_출결_목록 = Arrays.asList(추가_근무가_없는_근무, 연장근무가_포함된_근무, 야간근무가_포함된_근무, 연장근무와_야간근무가_포함된_근무);
        final MonthlyAttendanceRequest 기준년월의_출결_목록_조회_요청 = 기준년월의_출결_목록_조회_요청_생성(조회월);
        final Pageable 조회_페이지_정보 = 기준년월의_출결_목록_조회_요청.getPageable();
        기준년월의_출결_목록_조회_제어(조회월, 조회_페이지_정보, 기준년월의_출결_목록);

        // When
        PageResponse<AttendanceResponse> 기준년월의_출결_목록_조회_응답 = attendanceService.findAttendanceResponsesByMonthly(기준년월의_출결_목록_조회_요청);

        // Then
        기준년월의_출결_목록_응답_검증(기준년월의_출결_목록, 기준년월의_출결_목록_조회_응답);
        verify(attendanceRepo).findAttendanceWithExtraWorksPageByMonthly(조회월, 조회_페이지_정보);
    }

    private void 기준년월의_출결_목록_조회_제어(YearMonth yearMonth, Pageable pageable, List<Attendance> attendances) {
        PageImpl<Attendance> pageResponse = new PageImpl<>(attendances, pageable, attendances.size());
        given(attendanceRepo.findAttendanceWithExtraWorksPageByMonthly(yearMonth, pageable)).willReturn(pageResponse);
    }

    private MonthlyAttendanceRequest 기준년월의_출결_목록_조회_요청_생성(YearMonth yearMonth) {
        final int requestPage = 0;
        final int perPageNum = 10;
        final String sortingProperties = "startAt";
        final Sort sort = Sort.by(Direction.DESC, sortingProperties);
        return MonthlyAttendanceRequest.of(yearMonth, PageRequest.of(requestPage, perPageNum, sort));
    }

    private void 기준년월의_출결_목록_응답_검증(List<Attendance> attendances, PageResponse<AttendanceResponse> attendanceResponses) {
        List<Long> collect = attendances.stream()
            .map(Attendance::getId)
            .collect(Collectors.toList());

        List<AttendanceResponse> elements = attendanceResponses.getElements();
        List<Long> attendanceResponseIds = elements.stream()
            .map(AttendanceResponse::getId)
            .collect(Collectors.toList());

        assertAll(
            () -> assertThat(attendanceResponses.getTotalPages()).as("전체 페이지 수").isEqualTo(1),
            () -> assertThat(attendanceResponses.getCurrentElementCount()).as("조회된 페이지의 컨텐츠 수").isEqualTo(attendances.size()),
            () -> assertThat(attendanceResponses.getCurrentPage()).as("현재 페이지 번호").isEqualTo(1),
            () -> assertThat(attendanceResponses.getPerPageNumber()).as("페이지당 컨텐츠 수").isEqualTo(10),
            () -> assertThat(attendanceResponseIds).containsExactlyElementsOf(collect)
        );
    }
}
