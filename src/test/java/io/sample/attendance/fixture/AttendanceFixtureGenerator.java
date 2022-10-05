package io.sample.attendance.fixture;

import static org.hibernate.type.IntegerType.ZERO;

import io.sample.attendance.domain.Attendance;
import io.sample.attendance.dto.AttendanceDto.AttendanceRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AttendanceFixtureGenerator {
    private static final LocalDate today = LocalDate.now();
    private static final LocalDate nextDay = today.plusDays(1);

    public static Attendance 일반_근무() {
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(9, ZERO));
        final LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(18, ZERO));
        return Attendance.of(startAt, endAt);
    }

    public static Attendance 연장근무가_포함된_근무() {
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(9, ZERO));
        final LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(22, ZERO));
        return Attendance.of(startAt, endAt);
    }

    public static Attendance 야간근무가_포함된_근무() {
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(9, ZERO));
        final LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(23, 30));
        return Attendance.of(startAt, endAt);
    }

    public static Attendance 연장근무와_야간근무가_포함된_근무() {
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(5, ZERO));
        final LocalDateTime endAt = LocalDateTime.of(nextDay, LocalTime.of(1, ZERO));
        return Attendance.of(startAt, endAt);
    }
}
