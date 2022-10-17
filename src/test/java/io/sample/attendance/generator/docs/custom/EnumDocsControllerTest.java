package io.sample.attendance.generator.docs.custom;

import static io.sample.attendance.generator.docs.custom.EnumDocumentGenerator.extraWorkTypeDocument;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.sample.attendance.config.SpringBootTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("API:Enum Code")
public class EnumDocsControllerTest extends SpringBootTestBase {
    private static final String ENUM_DOCS_URL = "/enum";
    private static final String EXTRA_WORK_TYPE_ENUM_DOCS_URL = ENUM_DOCS_URL.concat("/extra-work-type");

    private void 공통_항목_응답_구조_조회_응답_검증(ResultActions 공통_항목_응답_구조_조회_응답) throws Exception {
        공통_항목_응답_구조_조회_응답.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Enum 상태 코드:EXTRA_WORK_TYPE")
    public void Enum_상태_코드() throws Exception {
        // When
        ResultActions 추가_근무_타입_열거형_코드_조회_응답 = 열거형_코드_조회_요청(EXTRA_WORK_TYPE_ENUM_DOCS_URL);

        // Then
        공통_항목_응답_구조_조회_응답_검증(추가_근무_타입_열거형_코드_조회_응답);
        공통_열겨형_응답_구조_문서_생성(추가_근무_타입_열거형_코드_조회_응답);
    }

    private ResultActions 열거형_코드_조회_요청(String url) throws Exception {
        return get(url);
    }

    private void 공통_열겨형_응답_구조_문서_생성(ResultActions 공통_열거형_응답_구조_조회_응답) throws Exception {
        공통_열거형_응답_구조_조회_응답.andDo(extraWorkTypeDocument());
    }
}
