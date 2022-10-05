package io.sample.attendance.dto;

import io.sample.attendance.domain.ExtraWorks;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExtraWorksResponse {
    private List<ExtraWorkResponse> elements;
    private int totalExtraPay;

    private ExtraWorksResponse(List<ExtraWorkResponse> elements, int totalExtraPay) {
        this.elements = elements;
        this.totalExtraPay = totalExtraPay;
    }

    public static ExtraWorksResponse from(ExtraWorks extraWorks) {
        return new ExtraWorksResponse(
            extraWorks.getElements()
                .stream()
                .map(ExtraWorkResponse::from)
                .collect(Collectors.toList()),
            extraWorks.getTotalExtraPay()
        );
    }
}
