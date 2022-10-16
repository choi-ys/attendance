package io.sample.attendance.generator.docs.common;

import io.sample.attendance.model.domain.ExtraWorkType;
import io.sample.global.model.EnumType;
import io.sample.attendance.generator.docs.custom.EnumDocument;
import io.sample.global.response.PageResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = "common",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class CommonDocsSnippetController {
    @GetMapping("pagination")
    public <E> ResponseEntity<PageResponse<List<E>>> commonPaginationResponse() {
        return ResponseEntity.ok(generateCommonPageResponse());
    }

    private <E> PageResponse<List<E>> generateCommonPageResponse() {
        return PageResponse.of(generateDefaultPage());
    }

    private <E> Page<List<E>> generateDefaultPage() {
        return new PageImpl<>(new ArrayList<>());
    }

    @GetMapping("enum")
    public ResponseEntity<EnumDocument> enumDocs() {
        return ResponseEntity.ok(generateCommonEnumDocument());
    }

    private EnumDocument generateCommonEnumDocument() {
        return EnumDocument.of(
            getDocs(ExtraWorkType.values())
        );
    }

    private Map<String, String> getDocs(EnumType[] enumTypes) {
        return Arrays.stream(enumTypes)
            .collect(Collectors.toMap(
                EnumType::getName,
                EnumType::getDescription
            ));
    }
}
