package io.sample.attendance.global.exception;

import io.sample.attendance.global.response.ErrorCode;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(ErrorCode errorCode, Object param) {
        super(errorCode, param);
    }
}
