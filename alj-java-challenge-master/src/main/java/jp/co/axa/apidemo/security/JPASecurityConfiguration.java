package jp.co.axa.apidemo.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class is the Heart of enabling spring security for each REST method that is defined in the controller.
 * EnableWebSecurity is responsible for making that happen.
 * Also by enabling the GlobalMethodLevelSecurity we emphasize the authentication and authorization for each method of Rest Controller.
 */
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JPASecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    UserDetailsProviderService userDetailsProviderService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //disabling the cross site reference is important to stop attacks CSRF attacks.
        //alternate to below line could be --> http.headers().frameOptions().sameOrigin();
        http.authorizeRequests()
                .antMatchers("/h2-console/**", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().ignoringAntMatchers("/h2-console/**", "/swagger-ui/**").disable().headers().frameOptions().disable()
                .and().httpBasic().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    // if you don't provide a password encoder, spring security will throw a RuntimeException:IllegalArgumentException There is no PasswordEncoder mapped for the id "null".
    @Bean
    public PasswordEncoder noOpPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * Somce we are using JDBC(h2) based User Config we need this DaoAuthenticationManager which will then set the userDetailsProviderService and a noOpPasswordEncoder.
     *
     * @return DaoAuthenticationProvider further to be used for fetching User details.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsProviderService);
        authProvider.setPasswordEncoder(noOpPasswordEncoder());

        return authProvider;
    }
}