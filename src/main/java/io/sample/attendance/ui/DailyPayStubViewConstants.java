package io.sample.attendance.ui;

import java.text.DecimalFormat;

public interface DailyPayStubViewConstants {
    String START_AT = "출근 : ";
    String END_AT = "퇴근 : ";
    String SPLIT_LINE = "--------------------";
    String TOTAL_PAY = "급여 총액 : ";
    String TOTAL_WORKING_TIME = "총 근로 시간 : ";
    String OVERTIME_WORKING_TIME = "연장 근무 시간 : ";
    String OVERTIME_EXTRA_PAY = "연장 근로 수당 : ";
    String NIGHT_SHIFT_WORKING_TIME = "야간 근무 시간 : ";
    String NIGHT_SHIFT_EXTRA_PAY = "야간 근로 수당 : ";
    String LINE_SEPARATOR = System.lineSeparator();
    String TILDE = " ~ ";
    String COMMA = ", ";
    DecimalFormat decimalFormatter = new DecimalFormat("###,###");
}
