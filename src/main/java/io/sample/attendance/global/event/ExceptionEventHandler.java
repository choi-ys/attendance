package io.sample.attendance.global.event;

import io.sample.attendance.global.exception.BusinessException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExceptionEventHandler {
    @EventListener
    @Async
    public void onExceptionEventListener(ThrowsException throwsException) {
        BusinessException exception = throwsException.getException();
        HttpServletRequest request = throwsException.getRequest();
        log.error("[method : {}, uri : {}]\n[code :{}, message : {}]\n[trace : {}]",
            request.getMethod(), request.getRequestURI(),
            exception.getErrorCodeName(), exception.getMessage(),
            throwsException.getAbbreviatedExceptionStaceTrace()
        );
    }
}
