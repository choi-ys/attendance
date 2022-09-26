package io.sample.attendance.ui;

import static io.sample.attendance.ui.DailyPayStubViewConstants.COMMA;
import static io.sample.attendance.ui.DailyPayStubViewConstants.END_AT;
import static io.sample.attendance.ui.DailyPayStubViewConstants.LINE_SEPARATOR;
import static io.sample.attendance.ui.DailyPayStubViewConstants.NIGHT_SHIFT_EXTRA_PAY;
import static io.sample.attendance.ui.DailyPayStubViewConstants.NIGHT_SHIFT_WORKING_TIME;
import static io.sample.attendance.ui.DailyPayStubViewConstants.OVERTIME_EXTRA_PAY;
import static io.sample.attendance.ui.DailyPayStubViewConstants.OVERTIME_WORKING_TIME;
import static io.sample.attendance.ui.DailyPayStubViewConstants.SPLIT_LINE;
import static io.sample.attendance.ui.DailyPayStubViewConstants.START_AT;
import static io.sample.attendance.ui.DailyPayStubViewConstants.TILDE;
import static io.sample.attendance.ui.DailyPayStubViewConstants.TOTAL_PAY;
import static io.sample.attendance.ui.DailyPayStubViewConstants.TOTAL_WORKING_TIME;
import static io.sample.attendance.ui.DailyPayStubViewConstants.decimalFormatter;
import static org.hibernate.type.IntegerType.ZERO;

import io.sample.attendance.domain.Attendance;
import io.sample.attendance.domain.NightShift;
import io.sample.attendance.domain.Overtime;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DailyPayStubView {
    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void printDailyPayStub(Attendance attendance) {
        String stringBuffer = START_AT + removeT(attendance.getStartAt()) + LINE_SEPARATOR
            + END_AT + removeT(attendance.getEndAt()) + LINE_SEPARATOR
            + SPLIT_LINE + LINE_SEPARATOR
            + TOTAL_PAY + addKRW(attendance.getTotalPay()) + LINE_SEPARATOR
            + TOTAL_WORKING_TIME + workingTimeToString(attendance.getWorkingTime())
            + LINE_SEPARATOR
            + overtimeGuideMessage(attendance.getOvertime())
            + nightShiftGuideFormatter(attendance.getNightShift());
        System.out.println(stringBuffer);
    }

    private static String overtimeGuideMessage(Overtime overtime) {
        return additionalWorkingFormatter(
            overtime.getWorkingTime(),
            OVERTIME_WORKING_TIME,
            overtime.getStartTime(),
            overtime.getEndTime(),
            OVERTIME_EXTRA_PAY,
            overtime.getExtraPay()
        );
    }

    private static String nightShiftGuideFormatter(NightShift nightShift) {
        return additionalWorkingFormatter(
            nightShift.getWorkingTime(),
            NIGHT_SHIFT_WORKING_TIME,
            nightShift.getStartTime(),
            nightShift.getEndTime(),
            NIGHT_SHIFT_EXTRA_PAY,
            nightShift.getExtraPay()
        );
    }

    private static String additionalWorkingFormatter(
        LocalTime workingTime,
        String nightShiftWorkingTime,
        LocalTime startTime,
        LocalTime endTime,
        String nightShiftExtraPay,
        int extraPay
    ) {
        if (hasNoAdditionalWork(workingTime)) {
            return "";
        }
        return nightShiftWorkingTime
            + workingTimeToString(workingTime) + COMMA
            + startTime + TILDE + endTime + LINE_SEPARATOR
            + nightShiftExtraPay + addKRW(extraPay) + LINE_SEPARATOR;
    }

    private static boolean hasNoAdditionalWork(LocalTime workingTime) {
        return workingTime.getHour() == ZERO && workingTime.getMinute() == ZERO;
    }

    private static String workingTimeToString(LocalTime workingTime) {
        return String.format("%d시간 %d분", workingTime.getHour(), workingTime.getMinute());
    }

    private static String addKRW(int amount) {
        String format = decimalFormatter.format(amount);
        return String.format("%s원", format);
    }

    private static String removeT(LocalDateTime target) {
        return dateTimeFormatter.format(target);
    }
}
