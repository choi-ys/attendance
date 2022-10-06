package io.sample.attendance.global.exception;

import io.sample.attendance.global.response.ErrorCode;

public class InValidTimeException extends BusinessException {
    public InValidTimeException(ErrorCode errorCode, Object... param) {
        super(errorCode, param);
    }
}
