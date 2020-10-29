package jp.co.axa.apidemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class JPASecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    UserDetailsProviderService userDetailsProviderService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsProviderService).passwordEncoder(noOpPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //disabling the cross site reference is important to stop attacks CSRF attacks.
        //alternate to below line could be --> http.headers().frameOptions().sameOrigin();
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "**/api/v1/employees**/").hasAnyRole("ROLE_ADMIN", "ROLE_EDITOR")
                .antMatchers(HttpMethod.DELETE, "**/api/v1/employees/**").hasAnyRole("ROLE_ADMIN")
                .antMatchers(HttpMethod.PUT, "**/api/v1/employees/**)").hasAnyRole("ROLE_ADMIN", "ROLE_EDITOR")
                .antMatchers(HttpMethod.GET, "**/api/v1/employees/**").permitAll()
                .anyRequest().permitAll()
                .and().csrf().disable().headers().frameOptions().disable().and().httpBasic();
    }

    // if you don't provide a password encoder, spring security will throw a RuntimeException:IllegalArgumentException There is no PasswordEncoder mapped for the id "null".
    @Bean
    public PasswordEncoder noOpPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
