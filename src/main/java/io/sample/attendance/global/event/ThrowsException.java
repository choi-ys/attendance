package io.sample.attendance.global.event;

import io.sample.attendance.global.response.ErrorCode;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;

@Getter
public class ThrowsException {
    private static final int MAXIMUM_ABBREVIATED_STACK_TRACE_COUNT = 5;

    private Exception exception;
    private HttpServletRequest Request;
    private ErrorCode errorCode;

    protected ThrowsException(HttpServletRequest Request) {
        this.Request = Request;
    }

    private ThrowsException(Exception exception, HttpServletRequest Request, ErrorCode errorCode) {
        this.exception = exception;
        this.Request = Request;
        this.errorCode = errorCode;
    }

    public static ThrowsException of(Exception exception, HttpServletRequest Request, ErrorCode errorCode) {
        return new ThrowsException(exception, Request, errorCode);
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
