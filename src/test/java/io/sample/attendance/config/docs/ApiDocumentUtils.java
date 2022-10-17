package io.sample.attendance.config.docs;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.epages.restdocs.apispec.ResourceSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.snippet.Attributes.Attribute;
import org.springframework.restdocs.snippet.Snippet;

public interface ApiDocumentUtils {
    String BASE_HOST = "attendance.io";
    String DOCUMENT_IDENTIFIER = "{class-name}/{method-name}";

    static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
            modifyUris()
                .scheme(scheme())
                .host(host())
                .removePort(),
            prettyPrint());
    }

    static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(
            modifyUris()
                .scheme(scheme())
                .host(host())
                .removePort(),
            prettyPrint());
    }

    static RestDocumentationResultHandler createDocumentBySnippets(Snippet... snippets) {
        return document(
            DOCUMENT_IDENTIFIER,
            getDocumentRequest(),
            getDocumentResponse(),
            snippets
        );
    }

    static RestDocumentationResultHandler createDocumentByResourceSnippet(ResourceSnippet resourceSnippet) {
        return document(DOCUMENT_IDENTIFIER, resourceSnippet);
    }

    private static String scheme() {
        return "http";
    }

    private static String host() {
        return String.format("dev-api.%s", BASE_HOST);
    }

    static Attribute format(String value) {
        return new Attribute("format", value);
    }
}
