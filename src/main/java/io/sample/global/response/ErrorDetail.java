package io.sample.global.response;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorDetail {
    private String object;
    private String field;
    private String code;
    private String rejectMessage;
    private Object rejectedValue;

    public static List<ErrorDetail> from(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
            .map(it ->
                new ErrorDetail(
                    it.getObjectName(),
                    it.getField(),
                    it.getCode(),
                    it.getDefaultMessage(),
                    it.getRejectedValue()
                )
            ).collect(Collectors.toList());
    }
}
