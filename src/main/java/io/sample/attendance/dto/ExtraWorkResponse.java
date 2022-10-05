package io.sample.attendance.dto;

import io.sample.attendance.domain.ExtraWork;
import io.sample.attendance.domain.ExtraWorkType;
import io.sample.attendance.domain.WorkDuration;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExtraWorkResponse {
    private Long id;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private WorkDuration workDuration;
    private ExtraWorkType extraWorkType;
    private int extraPay;

    private ExtraWorkResponse(
        Long id,
        LocalDateTime startAt,
        LocalDateTime endAt,
        WorkDuration workDuration,
        ExtraWorkType extraWorkType,
        int extraPay
    ) {
        this.id = id;
        this.startAt = startAt;
        this.endAt = endAt;
        this.workDuration = workDuration;
        this.extraWorkType = extraWorkType;
        this.extraPay = extraPay;
    }

    public static ExtraWorkResponse from(ExtraWork extraWork) {
        return new ExtraWorkResponse(
            extraWork.getId(),
            extraWork.getStartAt(),
            extraWork.getEndAt(),
            extraWork.getWorkDuration(),
            extraWork.getExtraWorkType(),
            extraWork.getPay()
        );
    }
}
