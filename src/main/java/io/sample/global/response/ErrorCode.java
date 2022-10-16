package io.sample.global.response;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

import lombok.Getter;

@Getter
public enum ErrorCode {
    HTTP_MESSAGE_NOT_READABLE(BAD_REQUEST.value(), "요청값을 확인 할 수 없습니다. 요청값을 확인해주세요."),
    METHOD_ARGUMENT_TYPE_MISMATCH(BAD_REQUEST.value(), "요청값의 자료형이 잘못되었습니다. 요청값을 확인해주세요."),
    METHOD_ARGUMENT_NOT_VALID(BAD_REQUEST.value(), "잘못된 요청입니다. 요청값을 확인해주세요."),
    MISSING_SERVLET_REQUEST_PARAMETER(BAD_REQUEST.value(), "잘못된 요청입니다. 요청값을 확인해주세요."),
    MISSING_SERVLET_PATH_VARIABLE(BAD_REQUEST.value(), "잘못된 요청입니다. 요청값을 확인해주세요."),
    RESOURCE_NOT_FOUND(NOT_FOUND.value(), "요청에 해당하는 자원을 찾을 수 없습니다. : %s"),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED(METHOD_NOT_ALLOWED.value(), "허용하지 않는 Http Method 요청입니다."),
    HTTP_MEDIA_TYPE_NOT_ACCEPTABLE(NOT_ACCEPTABLE.value(), "지원하지 않는 Accept Type 입니다."),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED(UNSUPPORTED_MEDIA_TYPE.value(), "지원하지 않는 Media Type 입니다."),
    UNEXPECTED_ERROR(INTERNAL_SERVER_ERROR.value(), "알 수 없는 오류가 발생하였습니다.");

    public int httpStatus;
    public String message;

    ErrorCode(int httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static ErrorCode valueOf(Exception exception) {
        String errorCodeName = convertExceptionClassNameToErrorCodeName(exception);
        try {
            return valueOf(errorCodeName);
        } catch (IllegalArgumentException e) {
            return ErrorCode.UNEXPECTED_ERROR;
        }
    }

    private static String convertExceptionClassNameToErrorCodeName(Exception exception) {
        String regex = "([a-z])([A-Z])";
        String replacement = "$1_$2";
        String exceptKeyword = "_Exception";
        String className = exception.getClass().getSimpleName();
        return className.replaceAll(regex, replacement)
            .replace(exceptKeyword, "")
            .toUpperCase();
    }
}
