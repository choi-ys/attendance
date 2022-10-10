package io.sample.attendance.domain;

import io.sample.attendance.global.auditor.BaseEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExtraWork extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
        name = "attendance_id",
        foreignKey = @ForeignKey(name = "FK_EXTRA_WORK_TO_ATTENDANCE"),
        nullable = false
    )
    private Attendance attendance;

    @Embedded
    private TimeTable timeTable;

    @Enumerated(EnumType.STRING)
    private ExtraWorkType extraWorkType;
    private int pay;

    private ExtraWork(Attendance attendance, LocalDateTime startAt, LocalDateTime endAt, ExtraWorkType extraWorkType) {
        this.attendance = attendance;
        this.timeTable = TimeTable.of(startAt, endAt);
        this.extraWorkType = extraWorkType;
        int totalMinute = timeTable.getWorkDurationByMinute();
        this.pay = extraWorkType.calculateExtraPay(totalMinute);
    }

    public static ExtraWork of(Attendance attendance, LocalDateTime startAt, LocalDateTime endAt, ExtraWorkType nightShift) {
        return new ExtraWork(attendance, startAt, endAt, nightShift);
    }

    public boolean isNightShift() {
        return extraWorkType.isNightShift();
    }

    public boolean isOvertime() {
        return extraWorkType.isOvertime();
    }

    public WorkDuration getWorkDuration() {
        return timeTable.getWorkDuration();
    }

    public LocalDateTime getStartAt() {
        return timeTable.getStartAt();
    }

    public LocalDateTime getEndAt() {
        return timeTable.getEndAt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExtraWork extraWork = (ExtraWork) o;
        return pay == extraWork.pay &&
            Objects.equals(timeTable, extraWork.timeTable) &&
            extraWorkType == extraWork.extraWorkType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeTable, extraWorkType, pay);
    }
}
