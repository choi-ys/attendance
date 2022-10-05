package io.sample.attendance.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;

@Getter
public class ExtraWork {
    private TimeTable timeTable;
    private ExtraWorkType extraWorkType;
    private int pay;

    private ExtraWork(LocalDateTime startAt, LocalDateTime endAt, ExtraWorkType extraWorkType) {
        this.timeTable = TimeTable.of(startAt, endAt);
        this.extraWorkType = extraWorkType;
        int totalMinute = timeTable.getWorkDurationByMinute();
        this.pay = extraWorkType.calculateExtraPay(totalMinute);
    }

    public static ExtraWork of(LocalDateTime startAt, LocalDateTime endAt, ExtraWorkType nightShift) {
        return new ExtraWork(startAt, endAt, nightShift);
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
