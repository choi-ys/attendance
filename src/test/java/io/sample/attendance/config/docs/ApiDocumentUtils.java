package io.sample.attendance.config.docs;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;

import com.epages.restdocs.apispec.ResourceSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.snippet.Attributes.Attribute;
import org.springframework.restdocs.snippet.Snippet;

public interface ApiDocumentUtils {
    String DOCUMENT_IDENTIFIER = "{class-name}/{method-name}";

    static RestDocumentationResultHandler createDocumentBySnippets(Snippet... snippets) {
        return document(DOCUMENT_IDENTIFIER, snippets);
    }

    static RestDocumentationResultHandler createDocumentByResourceSnippet(ResourceSnippet resourceSnippet) {
        return document(DOCUMENT_IDENTIFIER, resourceSnippet);
    }

    static Attribute format(String value) {
        return new Attribute("format", value);
    }
}
