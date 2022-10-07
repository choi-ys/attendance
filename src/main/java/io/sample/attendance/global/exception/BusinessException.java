package io.sample.attendance.global.exception;

import io.sample.attendance.global.response.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, Object... param) {
        super(String.format(errorCode.message, param));
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String message, Object... param) {
        super(String.format(message, param));
        this.errorCode = errorCode;
    }

    public int getHttpStatus() {
        return errorCode.getHttpStatus();
    }

    public String getErrorCodeName() {
        return errorCode.name();
    }
}
