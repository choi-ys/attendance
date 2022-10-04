package io.sample.attendance.domain;

import io.sample.attendance.utils.NightShiftCalculator;
import io.sample.attendance.utils.OvertimeCalculator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExtraWorks {
    List<ExtraWork> elements = new ArrayList<>();

    public ExtraWorks(LocalDateTime startAt, LocalDateTime endAt) {
        if (NightShiftCalculator.isNightShift(startAt, endAt)) {
            elements.addAll(NightShiftCalculator.extract(startAt, endAt));
        }

        if (OvertimeCalculator.isOverTime(startAt, endAt)) {
            elements.add(OvertimeCalculator.extract(startAt, endAt));
        }
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
}
