package io.sample.global.exception;

import io.sample.global.response.ErrorCode;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(ErrorCode errorCode, Object param) {
        super(errorCode, param);
    }
}
