package io.sample.attendance.global.exception;

import io.sample.attendance.global.response.ErrorCode;

public class InValidTimeException extends BusinessException {
    public InValidTimeException(String message, Object... param) {
        super(ErrorCode.METHOD_ARGUMENT_NOT_VALID, message, param);
    }
}
