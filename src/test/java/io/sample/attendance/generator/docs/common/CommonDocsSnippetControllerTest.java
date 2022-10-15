package io.sample.attendance.generator.docs.common;

import static io.sample.attendance.generator.docs.common.PaginationDocumentGenerator.generateCommonPaginationDocument;
import static io.sample.attendance.generator.docs.custom.EnumDocumentGenerator.generateMemberStatusDocument;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.sample.attendance.config.SpringBootTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("API:Common Response")
public class CommonDocsSnippetControllerTest extends SpringBootTestBase {
    private static final String COMMON_DOCS_URL = "/common";
    private static final String COMMON_PAGINATION_DOCS_URL = COMMON_DOCS_URL.concat("/pagination");
    private static final String COMMON_ENUM_DOCS_URL = COMMON_DOCS_URL.concat("/enum");

    @Test
    @DisplayName("공통 페이징 응답 구조")
    public void commonPaginationResponse() throws Exception {
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

    @Test
    @DisplayName("Enum 상태 코드")
    public void enumDocs() throws Exception {
        // When
        ResultActions 공통_열거형_응답_구조_조회_응답 = 공통_열거형_응답_구조_조회_요청();

        // Then
        공통_항목_응답_구조_조회_응답_검증(공통_열거형_응답_구조_조회_응답);
        공통_열겨형_응답_구조_문서_생성(공통_열거형_응답_구조_조회_응답);
    }

    private ResultActions 공통_열거형_응답_구조_조회_요청() throws Exception {
        return get(COMMON_ENUM_DOCS_URL);
    }

    private void 공통_열겨형_응답_구조_문서_생성(ResultActions 공통_열거형_응답_구조_조회_응답) throws Exception {
        공통_열거형_응답_구조_조회_응답.andDo(generateMemberStatusDocument(공통_열거형_응답_구조_조회_응답.andReturn()));
    }
}
