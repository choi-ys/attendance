package io.sample.attendance.generator.docs.custom;

import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumDocument {

    private Map<String, String> extraWorkType;

    public EnumDocument(Map<String, String> extraWorkType) {
        this.extraWorkType = extraWorkType;
    }

    public static EnumDocument of(Map<String, String> extraWorkType) {
        return new EnumDocument(extraWorkType);
    }
}
