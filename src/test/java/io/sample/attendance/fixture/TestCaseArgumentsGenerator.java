package io.sample.attendance.fixture;

import static org.hibernate.type.IntegerType.ZERO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class TestCaseArgumentsGenerator {
    private static final LocalDate today = LocalDate.now();
    private static final LocalDate nextDay = today.plusDays(1);

    public static Object[] sameDayWorkTestCaseArguments() {
        return new Object[]{
            "당일 퇴근이 아닌 경우",
            LocalDateTime.of(today, LocalTime.of(21, ZERO)),
            LocalDateTime.of(nextDay, LocalTime.of(5, ZERO))
        };
    }

    public static Object[] continuousTestCaseArguments() {
        return new Object[]{
            "당일 퇴근이고, 출근 혹은 퇴근 시간이 야간 근무 산정 범위에 포함되는 경우",
            LocalDateTime.of(today, LocalTime.of(5, ZERO)),
            LocalDateTime.of(today, LocalTime.of(17, ZERO))
        };
    }

    public static Object[] notContinuousTestCaseArguments() {
        return new Object[]{
            "당일 퇴근이고, 출/퇴근 시간 모두 야간 근무 산정 범위에 포함되는 경우",
            LocalDateTime.of(today, LocalTime.of(5, ZERO)),
            LocalDateTime.of(today, LocalTime.of(23, ZERO))
        };
    }

    public static <T> Object[] addArguments(Supplier<T> supplier, Object... arguments) {
        return Stream.of((Object[]) supplier.get(), arguments)
            .flatMap(Stream::of)
            .toArray(Object[]::new);
    }
}
