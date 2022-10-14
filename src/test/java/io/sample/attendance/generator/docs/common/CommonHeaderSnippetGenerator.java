package io.sample.attendance.generator.docs.common;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.headers.ResponseHeadersSnippet;

public class CommonHeaderSnippetGenerator {
    public static RequestHeadersSnippet defaultRequestHeaderSnippet() {
        return requestHeaders(
            headerWithName(HttpHeaders.ACCEPT).description("accept type header"),
            headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
        );
    }

    public static ResponseHeadersSnippet defaultResponseHeaderSnippet() {
        return responseHeaders(
            headerWithName(HttpHeaders.CONTENT_TYPE).description("Response content type")
        );
    }
}
