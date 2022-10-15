package io.sample.attendance.generator.fixture;

import static org.hibernate.type.IntegerType.ZERO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class TestCaseArgumentsGenerator {
    private static final LocalDate today = LocalDate.now();
    private static final LocalDate nextDay = today.plusDays(1);
    private static final LocalDate afterTwoDays = today.plusDays(2);

    public static Object[] notNightShift() {
        return new Object[]{
            "야간 근무가 없는 출결",
            LocalDateTime.of(today, LocalTime.of(6, ZERO)),
            LocalDateTime.of(today, LocalTime.of(22, ZERO))
        };
    }

    public static Object[] oneNightShiftSectionAndOneWorkDay() {
        return new Object[]{
            "당일 퇴근이고, 야간 근무 구간이 1개인 출결",
            LocalDateTime.of(today, LocalTime.of(6, ZERO)),
            LocalDateTime.of(today, LocalTime.of(22, 1))
        };
    }

    public static Object[] oneNightShiftSectionAndTwoWorkDay() {
        return new Object[]{
            "익일 퇴근이고, 야간 근무 구간이 1개인 출결",
            LocalDateTime.of(today, LocalTime.of(6, ZERO)),
            LocalDateTime.of(nextDay, LocalTime.of(6, 1))
        };
    }

    public static Object[] twoNightShiftSectionAndOneWorkDay() {
        return new Object[]{
            "당일 퇴근이고, 야간 근무 구간이 2개인 출결",
            LocalDateTime.of(today, LocalTime.of(5, 59)),
            LocalDateTime.of(today, LocalTime.of(22, 1)),
        };
    }

    public static Object[] twoNightShiftSectionAndTwoWorkDay() {
        return new Object[]{
            "익일 퇴근이고, 야간 근무 구간이 2개인 출결",
            LocalDateTime.of(today, LocalTime.of(22, 0)),
            LocalDateTime.of(nextDay, LocalTime.of(22, 1)),
        };
    }

    public static Object[] oneNightShiftSectionAndTwoWorkDayAndMaximumWork() {
        return new Object[]{
            "최대 28시간 근무, 익일 퇴근이고 야간 근무 구간이 1개인 출결",
            LocalDateTime.of(today, LocalTime.of(6, ZERO)),
            LocalDateTime.of(nextDay, LocalTime.of(10, ZERO)),
        };
    }

    public static Object[] twoNightShiftSectionAndTwoWorkDayAndMaximumWork() {
        return new Object[]{
            "최대 28시간 근무, 익일 퇴근이고 야간 근무 구간이 2개인 출결",
            LocalDateTime.of(today, LocalTime.of(20, 1)),
            LocalDateTime.of(nextDay, LocalTime.of(23, 59)),
        };
    }

    public static Object[] twoNightShiftSectionAndThreeWorkDayAndMaximumWork() {
        return new Object[]{
            "최대 28시간 근무, 2일 후 퇴근이고 야간 근무 구간이 2개인 출결",
            LocalDateTime.of(today, LocalTime.of(22, 0)),
            LocalDateTime.of(afterTwoDays, LocalTime.of(1, 59)),
        };
    }

    public static <T> Object[] add(Supplier<T> supplier, Object... arguments) {
        return Stream.of((Object[]) supplier.get(), arguments)
            .flatMap(Stream::of)
            .toArray(Object[]::new);
    }
}
