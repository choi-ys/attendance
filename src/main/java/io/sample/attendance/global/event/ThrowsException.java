package io.sample.attendance.global.event;

import io.sample.attendance.global.exception.BusinessException;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;

@Getter
public class ThrowsException {
    private static final int MAXIMUM_ABBREVIATED_STACK_TRACE_COUNT = 5;

    private BusinessException exception;
    private HttpServletRequest Request;

    private ThrowsException(BusinessException exception, HttpServletRequest Request) {
        this.exception = exception;
        this.Request = Request;
    }

    public static ThrowsException of(BusinessException exception, HttpServletRequest Request) {
        return new ThrowsException(exception, Request);
    }

    public String getAbbreviatedExceptionStaceTrace() {
        StringBuilder stringBuilder = new StringBuilder();
        StackTraceElement[] stackTraceElements = exception.getStackTrace();
        for (int i = 0; i < MAXIMUM_ABBREVIATED_STACK_TRACE_COUNT; i++) {
            stringBuilder.append(stackTraceElements[i]);
            if (isLastElement(i)) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    private boolean isLastElement(int index) {
        return index != MAXIMUM_ABBREVIATED_STACK_TRACE_COUNT - 1;
    }
}
