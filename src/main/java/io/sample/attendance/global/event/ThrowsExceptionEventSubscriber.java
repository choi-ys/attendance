package io.sample.attendance.global.event;

import io.sample.attendance.global.exception.BusinessException;
import io.sample.attendance.global.response.ErrorCode;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ThrowsExceptionEventSubscriber {
    @EventListener
    @Async
    public void onBusinessExceptionEventListener(ThrowsBusinessException throwsBusinessException) {
        BusinessException exception = throwsBusinessException.getException();
        HttpServletRequest request = throwsBusinessException.getRequest();
        log.error("[method : {}, uri : {}]\n[code :{}, message : {}]\n[trace : {}]",
            request.getMethod(), request.getRequestURI(),
            exception.getErrorCodeName(), exception.getMessage(),
            throwsBusinessException.getAbbreviatedExceptionStaceTrace()
        );
    }

    @EventListener
    @Async
    public void onBusinessExceptionEventListener(ThrowsException throwsException) {
        Exception exception = throwsException.getException();
        HttpServletRequest request = throwsException.getRequest();
        ErrorCode errorCode = throwsException.getErrorCode();
        log.error("[method : {}, uri : {}]\n[code :{}, message : {}, {}]\n[trace : {}]",
            request.getMethod(), request.getRequestURI(),
            errorCode.name(), errorCode.message, exception.getMessage(),
            throwsException.getAbbreviatedExceptionStaceTrace()
        );
    }
}
