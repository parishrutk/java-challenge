package jp.co.axa.apidemo.interceptors;

import org.slf4j.MDC;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.UUID;

public class RequestCorrelationInterceptor extends HandlerInterceptorAdapter {

    private static final String CORRELATION_HEADER = "X-Correlation-Id";
    private static final String CORRELATION_ID_LOG_VAR_NAME = "CorrelationId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String CORRELATION_ID = getCorrelationIDFromRequestHeader(request);
        MDC.put(CORRELATION_ID_LOG_VAR_NAME, CORRELATION_ID);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(CORRELATION_ID_LOG_VAR_NAME);
    }

    private String getCorrelationIDFromRequestHeader(HttpServletRequest request) {

        final String CORRELATION_ID = request.getHeader(CORRELATION_HEADER);
        if (Objects.nonNull(CORRELATION_ID)) {
            return CORRELATION_ID;
        } else {
            return UUID.randomUUID().toString();
        }
    }

}
