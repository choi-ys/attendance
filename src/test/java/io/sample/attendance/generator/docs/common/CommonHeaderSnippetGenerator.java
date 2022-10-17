package io.sample.attendance.generator.docs.common;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;

import java.util.Arrays;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.headers.ResponseHeadersSnippet;

public class CommonHeaderSnippetGenerator {
    public static RequestHeadersSnippet defaultRequestHeaderSnippet() {
        return requestHeaders(
            headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 Media Type"),
            headerWithName(HttpHeaders.ACCEPT).description("수신 Media Type")
        );
    }

    public static ResponseHeadersSnippet defaultResponseHeaderSnippet() {
        return responseHeaders(
            headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 Media Type")
        );
    }

    public static HeaderDescriptor[] defaultRequestHeaderDescriptors() {
        return Arrays.asList(
            headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 Media Type"),
            headerWithName(HttpHeaders.ACCEPT).description("수신 Media Type")
        ).toArray(HeaderDescriptor[]::new);
    }

    public static HeaderDescriptor defaultResponseHeaderDescriptor() {
        return headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 Media Type");
    }
}
