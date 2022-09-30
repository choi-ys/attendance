package io.sample.attendance.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ExtraWork {
    private TimeTable timeTable;
    private ExtraWorkType extraWorkType;
    private int pay;

    private ExtraWork(LocalDateTime startAt, LocalDateTime endAt, ExtraWorkType extraWorkType) {
        this.timeTable = TimeTable.of(startAt, endAt);
        this.extraWorkType = extraWorkType;
        int totalMinute = timeTable.getDurationByMinute();
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

    public WorkDuration getDuration() {
        return timeTable.getDuration();
    }
}
