package io.sample.attendance.global.event;

import io.sample.attendance.global.exception.BusinessException;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;

@Getter
public class ThrowsException {
    private BusinessException exception;
    private HttpServletRequest Request;

    private ThrowsException(BusinessException exception, HttpServletRequest Request) {
        this.exception = exception;
        this.Request = Request;
    }

    public static ThrowsException of(BusinessException exception, HttpServletRequest Request) {
        return new ThrowsException(exception, Request);
    }
}
