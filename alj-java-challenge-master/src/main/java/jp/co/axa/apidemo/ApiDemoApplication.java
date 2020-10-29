package jp.co.axa.apidemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "jp.co.axa.apidemo.repositories")
@EnableCaching
@ComponentScan(basePackages = "jp.co.axa.apidemo")
public class ApiDemoApplication {


    //for accessing H2 console
    //http://localhost:<port>>/h2-console
    public static void main(String[] args) {
        SpringApplication.run(ApiDemoApplication.class, args);
    }

}
