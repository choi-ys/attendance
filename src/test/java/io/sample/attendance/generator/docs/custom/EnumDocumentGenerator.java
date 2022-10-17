package io.sample.attendance.generator.docs.custom;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static io.sample.attendance.config.docs.ApiDocumentUtils.createDocumentByResourceSnippet;
import static io.sample.attendance.generator.docs.common.CommonHeaderSnippetGenerator.defaultRequestHeaderDescriptors;
import static io.sample.attendance.generator.docs.common.CommonHeaderSnippetGenerator.defaultResponseHeaderDescriptor;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import io.sample.attendance.model.domain.ExtraWorkType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;

public class EnumDocumentGenerator {
    public static final String EXTRA_WORK_TYPE_ACCESS_OPER = "extraWorkType.";

    public static RestDocumentationResultHandler extraWorkTypeDocument() {
        return createDocumentByResourceSnippet(
            resource(
                ResourceSnippetParameters.builder()
                    .summary("추가 근무 타입 코드")
                    .description("추가 근무 타입을 표현하는 ExtraWorkType 객체의 코드 목록")
                    .requestHeaders(defaultRequestHeaderDescriptors())
                    .responseHeaders(defaultResponseHeaderDescriptor())
                    .responseFields(
                        extraWorkTypeFieldDescriptors()
                    )
                    .build()
            )
        );
    }

    private static List<FieldDescriptor> extraWorkTypeFieldDescriptors() {
        return Arrays.stream(ExtraWorkType.values())
            .map(it -> fieldWithPath(EXTRA_WORK_TYPE_ACCESS_OPER.concat(it.getName())).description(it.getDescription()))
            .collect(Collectors.toList());
    }
}
