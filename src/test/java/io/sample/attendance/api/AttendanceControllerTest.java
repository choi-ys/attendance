package io.sample.attendance.api;

import static io.sample.attendance.generator.docs.AttendanceDocsGenerator.emptyRegisterAttendanceRequestDocument;
import static io.sample.attendance.generator.docs.AttendanceDocsGenerator.getAnAttendanceDocument;
import static io.sample.attendance.generator.docs.AttendanceDocsGenerator.getAttendancesDocument;
import static io.sample.attendance.generator.docs.AttendanceDocsGenerator.invalidRegisterAttendanceRequestDocument;
import static io.sample.attendance.generator.docs.AttendanceDocsGenerator.saveAttendanceDocument;
import static io.sample.attendance.generator.fixture.AttendanceFixtureGenerator.야간근무가_포함된_근무_생성_요청;
import static io.sample.attendance.generator.fixture.AttendanceFixtureGenerator.연장근무가_포함된_근무_생성_요청;
import static io.sample.attendance.generator.fixture.AttendanceFixtureGenerator.연장근무와_야간근무가_포함된_근무_생성_요청;
import static io.sample.attendance.generator.fixture.AttendanceFixtureGenerator.추가_근무가_없는_근무_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import io.sample.attendance.config.SpringBootTestBase;
import io.sample.attendance.dto.AttendanceDto.AttendanceRequest;
import io.sample.attendance.dto.AttendanceDto.AttendanceResponse;
import io.sample.attendance.global.response.PageResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@DisplayName("API:Attendance")
class AttendanceControllerTest extends SpringBootTestBase {
    public static final String ATTENDANCE_BASE_URL = "/attendance";
    public static final String GET_AN_ATTENDANCE_URL = "/attendance/{id}";
    public static final String GET_MONTHLY_ATTENDANCE_URL = ATTENDANCE_BASE_URL.concat("/monthly");
    public static final String YEAR_MONTH = "yearMonth";

    @Test
    @DisplayName("[201:POST]추가 근무가 없는 근태 기록 등록")
    public void registerAttendance() throws Exception {
        // When
        ResultActions 근무_기록_등록_응답 = 근무_기록_등록_요청(추가_근무가_없는_근무_생성_요청());

        // Then
        근무_기록_등록_응답_검증(근무_기록_등록_응답);
        추가_근무가_없는_근무_기록_응답_항목_검증(근무_기록_등록_응답);
    }

    @Test
    @DisplayName("[201:POST]추가 근무가 있는 근태 기록 등록")
    public void registerAttendanceWithExtraWorks() throws Exception {
        // When
        ResultActions 근무_기록_등록_응답 = 근무_기록_등록_요청(연장근무와_야간근무가_포함된_근무_생성_요청());

        // Then
        근무_기록_등록_응답_검증(근무_기록_등록_응답);
        추가_근무가_있는_근무_기록_응답_항목_검증(근무_기록_등록_응답);
        추가_근무가_있는_근태_기록_등록_문서_생성(근무_기록_등록_응답);
    }

    private ResultActions 근무_기록_등록_요청(AttendanceRequest 근무_기록_생성_요청) {
        return executeWithPersistContextClear(() -> post(ATTENDANCE_BASE_URL, 근무_기록_생성_요청));
    }

    private void 근무_기록_등록_응답_검증(ResultActions 근무_기록_등록_응답) throws Exception {
        근무_기록_등록_응답.andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().exists(HttpHeaders.LOCATION));
    }

    private void 추가_근무가_없는_근무_기록_응답_항목_검증(ResultActions 근무_기록_응답) throws Exception {
        근무_기록_응답
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.startAt").exists())
            .andExpect(jsonPath("$.endAt").exists())
            .andExpect(jsonPath("$.workDuration.hour").exists())
            .andExpect(jsonPath("$.workDuration.minute").exists())
            .andExpect(jsonPath("$.basicPay").exists())
            .andExpect(jsonPath("$.totalPay").exists())
            .andExpect(jsonPath("$.extraWorks").isEmpty());
    }

    private void 추가_근무가_있는_근태_기록_등록_문서_생성(ResultActions 근무_기록_등록_응답) throws Exception {
        근무_기록_등록_응답.andDo(saveAttendanceDocument());
    }

    private void 추가_근무가_있는_근무_기록_응답_항목_검증(ResultActions 근무_기록_응답) throws Exception {
        근무_기록_응답
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.startAt").exists())
            .andExpect(jsonPath("$.endAt").exists())
            .andExpect(jsonPath("$.workDuration.hour").exists())
            .andExpect(jsonPath("$.workDuration.minute").exists())
            .andExpect(jsonPath("$.basicPay").exists())
            .andExpect(jsonPath("$.totalPay").exists())
            .andExpect(jsonPath("$.extraWorks").exists())
            .andExpect(jsonPath("$.extraWorks.[*].id").exists())
            .andExpect(jsonPath("$.extraWorks.[*].startAt").exists())
            .andExpect(jsonPath("$.extraWorks.[*].endAt").exists())
            .andExpect(jsonPath("$.extraWorks.[*].workDuration").exists())
            .andExpect(jsonPath("$.extraWorks.[*].workDuration.hour").exists())
            .andExpect(jsonPath("$.extraWorks.[*].workDuration.minute").exists())
            .andExpect(jsonPath("$.extraWorks.[*].extraWorkType").exists())
            .andExpect(jsonPath("$.extraWorks.[*].extraPay").exists());
    }

    @Test
    @DisplayName("[400:POST]요청값이 없는 기록 등록")
    public void throwException_WhenEmptyAttendanceRequest() throws Exception {
        // When
        ResultActions 근무_기록_등록_응답 = 근무_기록_등록_요청(null);

        // Then
        요청값이_없는_출결_등록_응답_검증(근무_기록_등록_응답);
        요청값이_없는_출결_등록_문서_생성(근무_기록_등록_응답);
    }

    private void 요청값이_없는_출결_등록_응답_검증(ResultActions 근무_기록_등록_응답) throws Exception {
        근무_기록_등록_응답
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    private void 요청값이_없는_출결_등록_문서_생성(ResultActions 근무_기록_등록_응답) throws Exception {
        근무_기록_등록_응답.andDo(emptyRegisterAttendanceRequestDocument());
    }

    @Test
    @DisplayName("[400:POST]요청값이 없는 기록 등록")
    public void throwException_WhenNullAttendanceRequest() throws Exception {
        // Given
        AttendanceRequest 요청값이_없는_근무_생성_요청 = AttendanceRequest.of(null, null);

        // When
        ResultActions 근무_기록_등록_응답 = 근무_기록_등록_요청(요청값이_없는_근무_생성_요청);

        // Then
        요청값이_없는_기록_등록_응답_검증(근무_기록_등록_응답);
        요청값이_없는_기록_등록_문서_생성(근무_기록_등록_응답);
    }

    private void 요청값이_없는_기록_등록_응답_검증(ResultActions 근무_기록_등록_응답) throws Exception {
        근무_기록_등록_응답
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    private void 요청값이_없는_기록_등록_문서_생성(ResultActions 근무_기록_등록_응답) throws Exception {
        근무_기록_등록_응답.andDo(invalidRegisterAttendanceRequestDocument());
    }

    @Test
    @DisplayName("[400:POST]올바르지 않은 근태 기록 등록")
    public void throwException_WhenInvalidAttendanceRequest() throws Exception {
        // Given
        final LocalDateTime now = LocalDateTime.now();
        AttendanceRequest 출근시간과_퇴근시간이_같은_근무_생성_요청 = AttendanceRequest.of(now, now);

        // When
        ResultActions 근무_기록_등록_응답 = 근무_기록_등록_요청(출근시간과_퇴근시간이_같은_근무_생성_요청);

        // Then
        근무_기록_등록_응답
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[200:GET]추가 근무가 있는 근태 기록 조회")
    public void findAttendanceResponseById() throws Exception {
        // Given
        ResultActions 근무_기록_등록_응답 = 근무_기록_등록_요청(연장근무와_야간근무가_포함된_근무_생성_요청());
        Long 근무_번호 = as(근무_기록_등록_응답, AttendanceResponse.class).getId();

        // When
        ResultActions 근무_기록_조회_응답 = 근무_기록_조회_요청(근무_번호);

        // Then
        근무_기록_조회_응답_검증(근무_기록_조회_응답);
        추가_근무가_있는_근무_기록_응답_항목_검증(근무_기록_조회_응답);
        추가_근무가_있는_근태_기록_조회_문서_생성(근무_기록_조회_응답);
    }

    private ResultActions 근무_기록_조회_요청(Long id) throws Exception {
        return get(GET_AN_ATTENDANCE_URL, id);
    }

    private void 근무_기록_조회_응답_검증(ResultActions 근무_기록_조회_응답) throws Exception {
        근무_기록_조회_응답.andDo(print())
            .andExpect(status().isOk());
    }

    private void 추가_근무가_있는_근태_기록_조회_문서_생성(ResultActions 근무_기록_조회_응답) throws Exception {
        근무_기록_조회_응답.andDo(getAnAttendanceDocument());
    }

    @Test
    @DisplayName("[200:GET]특정월의 근태 목록 조회")
    public void findAttendancesMonthly() throws Exception {
        // Given
        List<AttendanceResponse> 특정월의_근무_목록_생성_응답 = 특정월의_근무_목록_생성();
        String 조회월 = YearMonth.from(LocalDate.now()).toString();

        // When
        ResultActions 특정월의_근태_목록_조회_응답 = 특정월의_근태_목록_조회_요청(조회월);

        // Then
        특정월의_근태_목록_조회_응답_검증(특정월의_근무_목록_생성_응답, 특정월의_근태_목록_조회_응답);
        특정월의_근태_목록_조회_문서_생성(특정월의_근태_목록_조회_응답);
    }

    private List<AttendanceResponse> 특정월의_근무_목록_생성() throws Exception {
        AttendanceResponse 근무_기록_등록_응답 = as(근무_기록_등록_요청(추가_근무가_없는_근무_생성_요청()), AttendanceResponse.class);
        AttendanceResponse 연장근무가_포함된_근무_등록_응답 = as(근무_기록_등록_요청(연장근무가_포함된_근무_생성_요청()), AttendanceResponse.class);
        AttendanceResponse 야간근무가_포함된_근무_등록_응답 = as(근무_기록_등록_요청(야간근무가_포함된_근무_생성_요청()), AttendanceResponse.class);
        AttendanceResponse 연장근무와_야간근무가_포함된_근무_등록_응답 = as(근무_기록_등록_요청(연장근무와_야간근무가_포함된_근무_생성_요청()), AttendanceResponse.class);
        entityManager.clear();
        return Arrays.asList(근무_기록_등록_응답, 연장근무가_포함된_근무_등록_응답, 야간근무가_포함된_근무_등록_응답, 연장근무와_야간근무가_포함된_근무_등록_응답);
    }

    private ResultActions 특정월의_근태_목록_조회_요청(String yearMonth) throws Exception {
        MultiValueMap<String, String> 조회_요청_정보 = new LinkedMultiValueMap<>();
        조회_요청_정보.add(YEAR_MONTH, yearMonth);
        return get(GET_MONTHLY_ATTENDANCE_URL, 조회_요청_정보);
    }

    private void 특정월의_근태_목록_조회_응답_검증(List<AttendanceResponse> 특정월의_근무_목록_생성_응답, ResultActions 특정월의_근태_목록_조회_응답) throws Exception {
        특정월의_근태_목록_조회_응답.andDo(print())
            .andExpect(status().isOk());

        List<Long> givenIds = 특정월의_근무_목록_생성_응답.stream()
            .map(AttendanceResponse::getId)
            .collect(Collectors.toList());

        List<Long> actualIds = as(특정월의_근태_목록_조회_응답, new TypeReference<PageResponse<AttendanceResponse>>() {
        })
            .getElements()
            .stream()
            .map(AttendanceResponse::getId)
            .collect(Collectors.toList());

        assertThat(actualIds).containsAll(givenIds);
    }

    private void 특정월의_근태_목록_조회_문서_생성(ResultActions resultActions) throws Exception {
        resultActions.andDo(getAttendancesDocument());
    }
}
