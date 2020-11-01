package jp.co.axa.apidemo.interceptors;

import org.slf4j.MDC;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.UUID;

/**
 * This class is responsible for Creating a unique correlationId and put it in each request in order to trace request flow.
 */
public class RequestCorrelationInterceptor extends HandlerInterceptorAdapter {

    private static final String CORRELATION_HEADER = "X-Correlation-Id";
    private static final String CORRELATION_ID_LOG_VAR_NAME = "CorrelationId";

    /**
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String CORRELATION_ID = getCorrelationIDFromRequestHeader(request);
        MDC.put(CORRELATION_ID_LOG_VAR_NAME, CORRELATION_ID); //MDC will contain the unique correlation id and will inject it in each request.
        return true;
    }

    /**
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    /**
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(CORRELATION_ID_LOG_VAR_NAME); //after the request has been completed remove the correlation id allocation.
    }

    /**
     *
     * @param request - takes in a HttpServletRequest to read the custom header parameter.
     * @return String - a Correlation Id representation using UUID.
     */
    private String getCorrelationIDFromRequestHeader(HttpServletRequest request) {

        final String CORRELATION_ID = request.getHeader(CORRELATION_HEADER);
        if (Objects.nonNull(CORRELATION_ID)) {
            return CORRELATION_ID;
        } else {
            return UUID.randomUUID().toString();
        }
    }

}
