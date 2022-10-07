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
    final int MAXIMUM_ABBREVIATED_STACK_TRACE_COUNT = 5;

    @EventListener
    @Async
    public void onExceptionEventListener(ThrowsException throwsException) {
        BusinessException exception = throwsException.getException();
        HttpServletRequest request = throwsException.getRequest();
        log.error("[method : {}, uri : {}]\n[code :{}, message : {}]\n[trace : {}]",
            request.getMethod(), request.getRequestURI(),
            exception.getErrorCodeName(), exception.getMessage(),
            getAbbreviatedExceptionStaceTrace(exception.getStackTrace())
        );
    }

    private String getAbbreviatedExceptionStaceTrace(StackTraceElement[] stackTraceElements) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < MAXIMUM_ABBREVIATED_STACK_TRACE_COUNT; i++) {
            stringBuilder.append(stackTraceElements[i]);
            if (isLastElement(i)) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    private boolean isLastElement(int index) {
        return index != MAXIMUM_ABBREVIATED_STACK_TRACE_COUNT - 1;
    }
}
