package jp.co.axa.apidemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "jp.co.axa.apidemo.repositories")
public class ApiDemoApplication {


    //for accessing H2 console
    //http://localhost:<port>>/h2-console
    public static void main(String[] args) {
        SpringApplication.run(ApiDemoApplication.class, args);
    }

}
