package io.sample.attendance.generator.docs.common;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static io.sample.attendance.config.docs.ApiDocumentUtils.createDocumentByResourceSnippet;
import static io.sample.attendance.generator.docs.common.CommonFieldDescriptor.commonPaginationFieldWithPath;
import static io.sample.attendance.generator.docs.common.CommonHeaderSnippetGenerator.defaultRequestHeaderDescriptors;
import static io.sample.attendance.generator.docs.common.CommonHeaderSnippetGenerator.defaultResponseHeaderDescriptor;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

public class PaginationDocumentGenerator {
    public static RestDocumentationResultHandler generateCommonPaginationDocument() {
        return createDocumentByResourceSnippet(
            resource(
                ResourceSnippetParameters.builder()
                    .description("공통 페이지 응답 구조")
                    .requestHeaders(defaultRequestHeaderDescriptors())
                    .responseHeaders(defaultResponseHeaderDescriptor())
                    .responseFields(
                        commonPaginationFieldWithPath()
                    )
                    .build()
            )
        );
    }
}
