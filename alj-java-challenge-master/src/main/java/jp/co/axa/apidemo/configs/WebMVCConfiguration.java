package jp.co.axa.apidemo.configs;

import jp.co.axa.apidemo.interceptors.RequestCorrelationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebMVCConfiguration implements WebMvcConfigurer {

    /**
     * The purpose of this class is to register an interceptor of the Type RequestCorrelationInterceptor which will inject a correlation-id in each request that comes to our controller.
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestCorrelationInterceptor());
    }
}