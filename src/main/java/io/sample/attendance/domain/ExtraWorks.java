package io.sample.attendance.domain;

import io.sample.attendance.utils.NightShiftCalculator;
import io.sample.attendance.utils.OvertimeCalculator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExtraWorks {
    @OneToMany(mappedBy = "attendance", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExtraWork> elements = new ArrayList<>();

    private ExtraWorks(Attendance attendance) {
        if (NightShiftCalculator.isNightShift(attendance)) {
            elements.addAll(NightShiftCalculator.extract(attendance));
        }

        if (OvertimeCalculator.isOverTime(attendance)) {
            elements.add(OvertimeCalculator.extract(attendance));
        }
    }

    public static ExtraWorks from(Attendance attendance) {
        return new ExtraWorks(attendance);
    }

    public int getSize() {
        return elements.size();
    }

    public int getTotalExtraPay() {
        return elements.stream()
            .mapToInt(ExtraWork::getPay)
            .sum();
    }

    public Set<ExtraWorkType> getExtraWorkTypes() {
        return elements.stream()
            .map(ExtraWork::getExtraWorkType)
            .collect(Collectors.toSet());
    }

    public ExtraWork getOverTime() {
        return elements.stream()
            .filter(ExtraWork::isOvertime)
            .findFirst()
            .orElse(null);
    }

    public List<ExtraWork> getNightShifts() {
        return elements.stream()
            .filter(ExtraWork::isNightShift)
            .collect(Collectors.toList());
    }

    public int getTotalNightShiftPay() {
        return getNightShifts().stream()
            .mapToInt(ExtraWork::getPay)
            .sum();
    }

    public WorkDuration getNightShiftDuration() {
        return elements.stream()
            .filter(ExtraWork::isNightShift)
            .map(ExtraWork::getWorkDuration)
            .reduce((x, y) -> WorkDuration.of(x.getHour() + y.getHour(), x.getMinute() + y.getMinute()))
            .orElseGet(WorkDuration::empty);
    }
}
