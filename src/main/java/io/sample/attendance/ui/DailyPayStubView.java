package io.sample.attendance.ui;

import static io.sample.attendance.ui.DailyPayStubViewConstants.END_AT;
import static io.sample.attendance.ui.DailyPayStubViewConstants.LINE_SEPARATOR;
import static io.sample.attendance.ui.DailyPayStubViewConstants.SPLIT_LINE;
import static io.sample.attendance.ui.DailyPayStubViewConstants.START_AT;
import static io.sample.attendance.ui.DailyPayStubViewConstants.TOTAL_PAY;
import static io.sample.attendance.ui.DailyPayStubViewConstants.TOTAL_WORKING_TIME;
import static io.sample.attendance.ui.DailyPayStubViewConstants.decimalFormatter;

import io.sample.attendance.domain.Attendance;
import io.sample.attendance.domain.ExtraWorks;
import io.sample.attendance.domain.WorkDuration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DailyPayStubView {
    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final StringBuffer stringBuffer = new StringBuffer();

    public static void printDailyPayStub(Attendance attendance) {
        attendanceGuideMessage(attendance);
        extraWorksGuideMessage(attendance.getExtraWorks());
        System.out.println(stringBuffer);
    }

    private static void attendanceGuideMessage(Attendance attendance) {
        stringBuffer.append(START_AT).append(removeT(attendance.getTimeTable().getStartAt())).append(LINE_SEPARATOR);
        stringBuffer.append(END_AT).append(removeT(attendance.getTimeTable().getEndAt())).append(LINE_SEPARATOR);
        stringBuffer.append(SPLIT_LINE).append(LINE_SEPARATOR);
        stringBuffer.append(TOTAL_PAY).append(addKRW(attendance.getTotalPay())).append(LINE_SEPARATOR);
        stringBuffer.append(TOTAL_WORKING_TIME).append(workingTimeToString(attendance.getWorkDuration())).append(LINE_SEPARATOR);
    }

    private static void extraWorksGuideMessage(ExtraWorks extraWorks) {
        stringBuffer.append(extraWorks);
    }

    private static String workingTimeToString(WorkDuration workDuration) {
        return String.format("%d시간 %d분", workDuration.getHour(), workDuration.getMinute());
    }

    private static String addKRW(int amount) {
        String format = decimalFormatter.format(amount);
        return String.format("%s원", format);
    }

    private static String removeT(LocalDateTime target) {
        return dateTimeFormatter.format(target);
    }
}
