package io.sample.attendance.exception;

import io.sample.global.exception.BusinessException;
import io.sample.global.response.ErrorCode;

public class InValidTimeException extends BusinessException {
    public InValidTimeException(String message, Object... param) {
        super(ErrorCode.METHOD_ARGUMENT_NOT_VALID, message, param);
    }
}
