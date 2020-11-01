package jp.co.axa.apidemo.configs;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class Swagger2UIConfiguration implements WebMvcConfigurer {

    @Bean
    public Docket swagger2Api() {
        //Register the controllers to swagger
        //In order to customize the Swagger Documentation we need to create a Docket object which then loads all the User defined configuration.
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .apis(Predicates.and(RequestHandlerSelectors.basePackage("jp.co.axa.apidemo")))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfo("Employee Management Service", "This Service will mainly be maintaining data related to Employees.",
                "v1.0", "Copyright to Axa Life Japan",
                new Contact("Axa Life Japan", "http://axalife.co.jp", "enquiry@axalife.co.jp"),
                "Private Limited", "http://axalife.co.jp/license", Collections.emptyList()));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //enabling swagger-ui part for visual documentation
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
