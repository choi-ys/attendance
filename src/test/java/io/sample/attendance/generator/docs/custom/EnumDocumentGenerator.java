package io.sample.attendance.generator.docs.custom;

import static io.sample.attendance.config.docs.ApiDocumentUtils.createDocument;
import static io.sample.attendance.generator.docs.custom.CustomResponseFieldsSnippet.customResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MvcResult;

public class EnumDocumentGenerator {

    private static final String ENUM_ADOC_TEMPLATE_PREFIX = "enum-response";
    private static final String ENUM_ADOC_TABLE_KEY = "title";
    public static final String EXTRA_WORK_TYPE = "extraWorkType";

    public static RestDocumentationResultHandler generateMemberStatusDocument(MvcResult mvcResult) throws IOException {
        EnumDocument enumDocument = parseEnumDocument(mvcResult);
        return createDocument(
            getEnumResponseFieldsSnippetByName(enumDocument, EXTRA_WORK_TYPE)
        );
    }

    private static CustomResponseFieldsSnippet getEnumResponseFieldsSnippetByName(EnumDocument enumDocument, String enumClassName) {
        return customResponseFields(
            ENUM_ADOC_TEMPLATE_PREFIX,
            beneathPath(enumClassName).withSubsectionId(enumClassName),
            attributes(key(ENUM_ADOC_TABLE_KEY).value(enumClassName)),
            enumConvertFieldDescriptor(enumDocument.getExtraWorkType())
        );
    }

    private static FieldDescriptor[] enumConvertFieldDescriptor(Map<String, String> enumValues) {
        return enumValues.entrySet().stream()
            .map(valueSet -> fieldWithPath(valueSet.getKey()).description(valueSet.getValue()))
            .toArray(FieldDescriptor[]::new);
    }

    public static EnumDocument parseEnumDocument(MvcResult mvcResult) throws IOException {
        return new ObjectMapper()
            .readValue(
                mvcResult.getResponse().getContentAsByteArray(),
                EnumDocument.class
            );
    }
}
