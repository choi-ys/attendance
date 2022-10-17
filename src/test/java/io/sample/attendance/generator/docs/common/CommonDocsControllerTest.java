package io.sample.attendance.generator.docs.common;

import static io.sample.attendance.generator.docs.common.PaginationDocumentGenerator.generateCommonPaginationDocument;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.sample.attendance.config.SpringBootTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("API:Common Response")
public class CommonDocsControllerTest extends SpringBootTestBase {
    private static final String COMMON_DOCS_URL = "/common";
    private static final String COMMON_PAGINATION_DOCS_URL = COMMON_DOCS_URL.concat("/pagination");

    @Test
    @DisplayName("공통 페이징 응답 구조")
    public void 공통_페이징_응답_구조() throws Exception {
        // When
        ResultActions 공통_페이징_응답_구조_조회_응답 = 공통_페이징_응답_구조_조회_요청();

        // Then
        공통_항목_응답_구조_조회_응답_검증(공통_페이징_응답_구조_조회_응답);
        공통_페이징_응답_구조_문서_생성(공통_페이징_응답_구조_조회_응답);
    }

    private ResultActions 공통_페이징_응답_구조_조회_요청() throws Exception {
        return get(COMMON_PAGINATION_DOCS_URL);
    }

    private void 공통_항목_응답_구조_조회_응답_검증(ResultActions 공통_항목_응답_구조_조회_응답) throws Exception {
        공통_항목_응답_구조_조회_응답.andExpect(status().isOk());
    }

    private void 공통_페이징_응답_구조_문서_생성(ResultActions 공통_페이징_응답_구조_조회_응답) throws Exception {
        공통_페이징_응답_구조_조회_응답.andDo(generateCommonPaginationDocument());
    }
}
