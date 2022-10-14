package io.sample.attendance.global.event;

import io.sample.attendance.global.exception.BusinessException;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;

@Getter
public class ThrowsBusinessException extends ThrowsException {
    private static final int MAXIMUM_ABBREVIATED_STACK_TRACE_COUNT = 5;

    private BusinessException exception;

    private ThrowsBusinessException(BusinessException exception, HttpServletRequest request) {
        super(request);
        this.exception = exception;
    }

    public static ThrowsBusinessException of(BusinessException exception, HttpServletRequest Request) {
        return new ThrowsBusinessException(exception, Request);
    }
}
