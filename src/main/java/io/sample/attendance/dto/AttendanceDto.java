package io.sample.attendance.dto;

import io.sample.attendance.domain.Attendance;
import io.sample.attendance.domain.ExtraWorks;
import io.sample.attendance.domain.WorkDuration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AttendanceDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class AttendanceRequest {
        private LocalDateTime startAt;
        private LocalDateTime endAt;

        private AttendanceRequest(LocalDateTime startAt, LocalDateTime endAt) {
            this.startAt = startAt;
            this.endAt = endAt;
        }

        public static AttendanceRequest of(LocalDateTime startAt, LocalDateTime endAt) {
            return new AttendanceRequest(startAt, endAt);
        }

        public Attendance toEntity() {
            return Attendance.of(startAt, endAt);
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class AttendanceResponse {
        private Long id;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private WorkDuration workDuration;
        private int basicPay;
        private int totalPay;
        private List<ExtraWorkResponse> extraWorks;

        public AttendanceResponse(
            Long id,
            LocalDateTime startAt,
            LocalDateTime endAt,
            WorkDuration workDuration,
            int basicPay,
            int totalPay,
            List<ExtraWorkResponse> extraWorks
        ) {
            this.id = id;
            this.startAt = startAt;
            this.endAt = endAt;
            this.workDuration = workDuration;
            this.basicPay = basicPay;
            this.totalPay = totalPay;
            this.extraWorks = extraWorks;
        }

        public static AttendanceResponse of(Attendance attendance) {
            return new AttendanceResponse(
                attendance.getId(),
                attendance.getStartAt(),
                attendance.getEndAt(),
                attendance.getWorkDuration(),
                attendance.getBasicPay(),
                attendance.getTotalPay(),
                of(attendance.getExtraWorks())
            );
        }

        private static List<ExtraWorkResponse> of(ExtraWorks extraWorks) {
            return extraWorks
                .getElements()
                .stream()
                .map(ExtraWorkResponse::from)
                .collect(Collectors.toList());
        }
    }
}
