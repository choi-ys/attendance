package io.sample.attendance.generator.docs.custom;

import io.sample.attendance.model.domain.ExtraWorkType;
import io.sample.global.model.EnumType;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = "enum",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class EnumDocsController {
    @GetMapping("extra-work-type")
    public ResponseEntity<EnumDocument> enumDocs() {
        return ResponseEntity.ok(generateCommonEnumDocument(ExtraWorkType.values()));
    }

    private EnumDocument generateCommonEnumDocument(EnumType[] enumType) {
        return EnumDocument.of(getEnumTypeValuesToMap(enumType));
    }

    private Map<String, String> getEnumTypeValuesToMap(EnumType[] enumTypes) {
        return Arrays.stream(enumTypes)
            .collect(Collectors.toMap(
                EnumType::getName,
                EnumType::getDescription
            ));
    }
}
