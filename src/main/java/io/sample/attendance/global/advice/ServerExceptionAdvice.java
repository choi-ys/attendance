package io.sample.attendance.global.advice;

import io.sample.attendance.global.exception.BusinessException;
import io.sample.attendance.global.response.ErrorCode;
import io.sample.attendance.global.response.ErrorResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ServerExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unexpectedException(Exception exception, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.UNEXPECTED_ERROR;
        return ResponseEntity
            .status(errorCode.httpStatus)
            .body(ErrorResponse.of(errorCode, request));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> businessException(BusinessException exception, HttpServletRequest request) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity
            .status(errorCode.httpStatus)
            .body(ErrorResponse.of(errorCode, request));
    }
}
