package io.sample.attendance.generator.docs.common;

import static io.sample.attendance.config.docs.ApiDocumentUtils.createDocument;
import static io.sample.attendance.generator.docs.common.CommonFieldDescriptor.commonPaginationFieldWithPath;
import static io.sample.attendance.generator.docs.common.CommonHeaderSnippetGenerator.defaultRequestHeaderSnippet;
import static io.sample.attendance.generator.docs.common.CommonHeaderSnippetGenerator.defaultResponseHeaderSnippet;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

public class PaginationDocumentGenerator {
    public static RestDocumentationResultHandler generateCommonPaginationDocument() {
        return createDocument(
            defaultRequestHeaderSnippet(),
            defaultResponseHeaderSnippet(),
            responseFields(
                commonPaginationFieldWithPath()
            )
        );
    }
}
