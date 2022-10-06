package io.sample.attendance.global.response;

import io.sample.attendance.global.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private String code;
    private String message;
    private String method;
    private String path;
    private List<ErrorDetail> errorDetails;
    private LocalDateTime timestamp = LocalDateTime.now();

    private ErrorResponse(ErrorCode errorCode, HttpServletRequest httpServletRequest) {
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
        this.method = httpServletRequest.getMethod();
        this.path = httpServletRequest.getRequestURI();
    }

    private ErrorResponse(ErrorCode errorCode, HttpServletRequest httpServletRequest, List<ErrorDetail> errorDetails) {
        this(errorCode, httpServletRequest);
        this.errorDetails = errorDetails;
    }

    private ErrorResponse(BusinessException businessException, HttpServletRequest httpServletRequest) {
        this.code = businessException.getErrorCodeName();
        this.message = businessException.getMessage();
        this.method = httpServletRequest.getMethod();
        this.path = httpServletRequest.getRequestURI();
    }

    public static ErrorResponse of(ErrorCode errorCode, HttpServletRequest httpServletRequest) {
        return new ErrorResponse(errorCode, httpServletRequest);
    }

    public static ErrorResponse of(ErrorCode errorCode, HttpServletRequest httpServletRequest, List<FieldError> fieldErrors) {
        return new ErrorResponse(errorCode, httpServletRequest, ErrorDetail.from(fieldErrors));
    }

    public static ErrorResponse of(BusinessException businessException, HttpServletRequest httpServletRequest) {
        return new ErrorResponse(businessException, httpServletRequest);
    }
}
