package io.sample.attendance.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExtraWorks {
    List<ExtraWork> elements = new ArrayList<>();

    public void add(ExtraWork extraWork) {
        this.elements.add(extraWork);
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
