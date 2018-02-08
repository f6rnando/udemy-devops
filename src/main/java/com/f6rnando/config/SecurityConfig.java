package com.f6rnando.config;

import com.f6rnando.backend.service.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.Arrays;
import java.util.List;

/************************************
 Created by f6rnando@gmail.com
 2018-02-07
 *************************************/

/*
This needs to be updated from the version 2.1.3.RELEASE to the latest
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    public Environment env;

    /**
     * Public allowed URLs
     */
    private static final String[] PUBLIC_MATCHES = {
            "/webjars/**", "/css/**", "/js/**", "/img/**",
            "/", "/about/**", "/contact/**", "/error/**/*",
            "/console/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        List<String> activeProfiles = Arrays.asList(env.getActiveProfiles());

        // Required for the H2 console to work correctly
        if (activeProfiles.contains("dev")) {
            http.csrf().disable();
            http.headers().frameOptions().disable();
        }

        http
                .authorizeRequests()
                .antMatchers(PUBLIC_MATCHES).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/payload")
                .failureUrl("/login?error").permitAll()
                .and()
                .logout().permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userSecurityService);
    }
}
