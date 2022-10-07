package io.sample.attendance.global.response;

import io.sample.attendance.global.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private LocalDateTime timestamp = LocalDateTime.now();
    private String method;
    private String path;
    private String code;
    private String message;
    private List<ErrorDetail> errorDetails;

    private ErrorResponse(String method, String path, String code, String message) {
        this.method = method;
        this.path = path;
        this.code = code;
        this.message = message;
    }

    private ErrorResponse(String method, String path, String code, String message, List<ErrorDetail> errorDetails) {
        this(method, path, code, message);
        this.errorDetails = errorDetails;
    }

    public static ErrorResponse businessErrorOf(BusinessException exception, HttpServletRequest request) {
        return new ErrorResponse(
            request.getMethod(),
            request.getRequestURI(),
            exception.getMessage(),
            exception.getErrorCodeName()
        );
    }

    public static ErrorResponse errorResponseOf(ErrorCode errorCode, HttpServletRequest request) {
        return new ErrorResponse(
            request.getMethod(),
            request.getRequestURI(),
            errorCode.message,
            errorCode.name()
        );
    }

    public static ErrorResponse errorResponseWithFieldErrorsOf(MethodArgumentNotValidException exception, ErrorCode errorCode, HttpServletRequest request) {
        return new ErrorResponse(
            request.getMethod(),
            request.getRequestURI(),
            errorCode.message,
            errorCode.name(),
            ErrorDetail.from(exception.getFieldErrors())
        );
    }
}
