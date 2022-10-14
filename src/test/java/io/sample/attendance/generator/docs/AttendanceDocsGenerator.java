package io.sample.attendance.generator.docs;

import static io.sample.attendance.config.docs.ApiDocumentUtils.createDocument;
import static io.sample.attendance.config.docs.ApiDocumentUtils.format;
import static io.sample.attendance.generator.docs.AttendanceFieldDescriptor.attendanceResponseFieldWithPath;
import static io.sample.attendance.generator.docs.common.CommonFieldDescriptor.commonErrorFieldWithPath;
import static io.sample.attendance.generator.docs.common.CommonFieldDescriptor.commonPaginationFieldWithPath;
import static io.sample.attendance.generator.docs.common.CommonFieldDescriptor.invalidErrorFieldWithPath;
import static io.sample.attendance.generator.docs.common.CommonHeaderSnippetGenerator.defaultRequestHeaderSnippet;
import static io.sample.attendance.generator.docs.common.CommonHeaderSnippetGenerator.defaultResponseHeaderSnippet;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;

public class AttendanceDocsGenerator {
    public static RestDocumentationResultHandler saveAttendanceDocument() {
        return createDocument(
            defaultRequestHeaderSnippet(),
            defaultResponseHeaderSnippet(),
            requestFields(
                fieldWithPath("startAt").description("근무 시작 시간").attributes(format("YYYY-MM-DD hh:mm:ss")),
                fieldWithPath("endAt").description("근무 종료 시간").attributes(format("YYYY-MM-DD hh:mm:ss"))
            ),
            responseFields(
                attendanceResponseFieldWithPath()
            )
        );
    }

    public static RestDocumentationResultHandler emptyRegisterAttendanceRequestDocument() {
        return createDocument(
            defaultRequestHeaderSnippet(),
            defaultResponseHeaderSnippet(),
            responseFields(
                commonErrorFieldWithPath()
            )
        );
    }

    public static RestDocumentationResultHandler invalidRegisterAttendanceRequestDocument() {
        return createDocument(
            defaultRequestHeaderSnippet(),
            defaultResponseHeaderSnippet(),
            requestFields(
                fieldWithPath("startAt").description("근무 시작 시간").attributes(format("YYYY-MM-DD hh:mm:ss")),
                fieldWithPath("endAt").description("근무 종료 시간").attributes(format("YYYY-MM-DD hh:mm:ss"))
            ),
            responseFields(
                Stream.concat(
                    commonErrorFieldWithPath().stream(),
                    invalidErrorFieldWithPath().stream()
                ).collect(Collectors.toList())
            )
        );
    }

    public static RestDocumentationResultHandler getAnAttendanceDocument() {
        return createDocument(
            defaultRequestHeaderSnippet(),
            defaultResponseHeaderSnippet(),
            pathParameters(
                parameterWithName("id").description("출결 번호")
            ),
            responseFields(
                attendanceResponseFieldWithPath()
            )
        );
    }

    public static RestDocumentationResultHandler getAttendancesDocument() {
        return createDocument(
            defaultRequestHeaderSnippet(),
            requestParameters(
                parameterWithName("yearMonth").description("조회 년월").attributes(format("YYYY-MM"))
            ),
            defaultResponseHeaderSnippet(),
            responseFields(
                Stream.concat(
                    commonPaginationFieldWithPath().stream(),
                    Stream.of(
                        fieldWithPath("elements[*].id").description("출결 번호"),
                        fieldWithPath("elements[*].startAt").description("근무 시작 시간"),
                        fieldWithPath("elements[*].endAt").description("근무 종료 시간"),
                        fieldWithPath("elements[*].workDuration.hour").description("근무 소요 시간"),
                        fieldWithPath("elements[*].workDuration.minute").description("근무 소요 분"),
                        fieldWithPath("elements[*].basicPay").description("기본 수당"),
                        fieldWithPath("elements[*].totalPay").description("전체 급여"),
                        fieldWithPath("elements[*].extraWorks[*]").description("추가 근무 목록"),
                        fieldWithPath("elements[*].extraWorks[*].id").description("추가 근무 번호"),
                        fieldWithPath("elements[*].extraWorks[*].startAt").description("추가 근무 시작 시간"),
                        fieldWithPath("elements[*].extraWorks[*].endAt").description("추가 근무 종료 시간"),
                        fieldWithPath("elements[*].extraWorks[*].workDuration.hour").description("추가 근무 소요 시간"),
                        fieldWithPath("elements[*].extraWorks[*].workDuration.minute").description("추가 근무 소요 분"),
                        fieldWithPath("elements[*].extraWorks[*].extraWorkType").description("추가 근무 타입"),
                        fieldWithPath("elements[*].extraWorks[*].extraPay").description("추가 수당")
                    )
                ).collect(Collectors.toList())
            )
        );
    }
}

class AttendanceFieldDescriptor {
    public static List<FieldDescriptor> attendanceResponseFieldWithPath() {
        return Arrays.asList(
            fieldWithPath("id").description("출결 번호"),
            fieldWithPath("startAt").description("근무 시작 시간"),
            fieldWithPath("endAt").description("근무 종료 시간"),
            fieldWithPath("workDuration.hour").description("근무 소요 시간"),
            fieldWithPath("workDuration.minute").description("근무 쇼요 분"),
            fieldWithPath("basicPay").description("기본 수당"),
            fieldWithPath("totalPay").description("전체 급여"),
            fieldWithPath("extraWorks[*].id").description("추가 근무 번호"),
            fieldWithPath("extraWorks[*].startAt").description("추가 근무 시작 시간"),
            fieldWithPath("extraWorks[*].endAt").description("추가 근무 종료 시간"),
            fieldWithPath("extraWorks[*].workDuration.hour").description("추가 근무 소요 시간"),
            fieldWithPath("extraWorks[*].workDuration.minute").description("추가 근무 소요 분"),
            fieldWithPath("extraWorks[*].extraWorkType").description("추가 근무 타입"),
            fieldWithPath("extraWorks[*].extraPay").description("추가 수당")
        );
    }
}
