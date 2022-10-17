package io.sample.attendance.model.dto;

import io.sample.attendance.model.domain.Attendance;
import io.sample.attendance.model.domain.ExtraWorks;
import io.sample.attendance.model.domain.TimeTable;
import io.sample.attendance.model.domain.WorkDuration;
import io.sample.global.utils.PageableUtils;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

public class AttendanceDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class AttendanceRequest {
        @Past(message = "근무 종료시간을 미리 등록할 수 없습니다.")
        @NotNull(message = "근무 시작시간은 빈값일 수 없습니다.")
        private LocalDateTime startAt;

        @Past(message = "근무 종료시간을 미리 등록할 수 없습니다.")
        @NotNull(message = "근무 종료시간은 빈값일 수 없습니다.")
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
            TimeTable attendanceTimeTable = TimeTable.of(attendance.getStartAt(), attendance.getEndAt());
            return new AttendanceResponse(
                attendance.getId(),
                attendanceTimeTable.getStartAt(),
                attendanceTimeTable.getEndAt(),
                attendanceTimeTable.getWorkDuration(),
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

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MonthlyAttendanceRequest {
        private YearMonth startAt;
        private Pageable pageable;

        private MonthlyAttendanceRequest(YearMonth startAt, Pageable pageable) {
            this.startAt = startAt;
            this.pageable = pageable;
        }

        public static MonthlyAttendanceRequest of(YearMonth startAt, Pageable pageable) {
            return new MonthlyAttendanceRequest(startAt, PageableUtils.pageNumberToIndex(pageable));
        }
    }
}
