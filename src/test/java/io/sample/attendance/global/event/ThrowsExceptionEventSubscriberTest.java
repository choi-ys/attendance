package io.sample.attendance.global.event;

import static io.sample.attendance.api.AttendanceController.ATTENDANCE_BASE_URL;
import static io.sample.attendance.validator.TimeValidator.NULL_VALUE_ERROR_MESSAGE;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.sample.attendance.global.exception.BusinessException;
import io.sample.attendance.global.exception.InValidTimeException;
import io.sample.attendance.global.response.ErrorCode;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

@SpringBootTest
@DisplayName("Global:Event:ThrowsExceptionEventSubscriber")
@TestConstructor(autowireMode = AutowireMode.ALL)
class ThrowsExceptionEventSubscriberTest {
    @MockBean
    private final ThrowsExceptionEventSubscriber throwsExceptionEventSubscriber;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ThrowsExceptionEventSubscriberTest(
        ApplicationEventPublisher applicationEventPublisher,
        ThrowsExceptionEventSubscriber throwsExceptionEventSubscriber
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.throwsExceptionEventSubscriber = throwsExceptionEventSubscriber;
    }

    @ParameterizedTest(name = "[Case#{index}]:{0}")
    @MethodSource
    @DisplayName("예외 발생 시, 오류 파악을 위한 이벤트 발행/수신")
    void onThrowsExceptionHandler(
        final String description,
        final ThrowsException 예외_발생_이벤트
    ) {
        // When
        예외_발생_시_이벤트_발행(예외_발생_이벤트);

        // Then
        예외_발생_이벤트_수신_검증(예외_발생_이벤트);
    }

    private void 예외_발생_이벤트_수신_검증(ThrowsException given) {
        verify(throwsExceptionEventSubscriber, times(1)).onBusinessExceptionEventListener(given);
    }

    private void 예외_발생_시_이벤트_발행(ThrowsException given) {
        applicationEventPublisher.publishEvent(given);
    }

    private static Stream<Arguments> onThrowsExceptionHandler() {
        final HttpServletRequest httpServletRequest = mockHttpServletRequest();
        return Stream.of(
            Arguments.of("의도적으로 발생시킨 사용자 정의 예외", ThrowsBusinessException.of(mockBusinessException(), httpServletRequest)),
            Arguments.of("의도하지 않은 예외", ThrowsException.of(mockException(), httpServletRequest, ErrorCode.UNEXPECTED_ERROR))
        );
    }

    private static HttpServletRequest mockHttpServletRequest() {
        final String requestMethod = HttpMethod.GET.name();
        return new MockHttpServletRequest(requestMethod, ATTENDANCE_BASE_URL);
    }

    private static BusinessException mockBusinessException() {
        return new InValidTimeException(NULL_VALUE_ERROR_MESSAGE);
    }

    private static Exception mockException() {
        return new Exception(ErrorCode.UNEXPECTED_ERROR.message);
    }
}
