package com.f6rnando.config;

import com.f6rnando.backend.service.EmailService;
import com.f6rnando.backend.service.MockEmailService;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/************************************
 Created by f6rnando@gmail.com
 2018-02-02
 *************************************/

@Configuration
@Profile("dev")
@PropertySource("file:///${user.home}/.f6rnando-devops/application-dev.properties")
public class DevelopmentConfig {

    @Bean
    public EmailService emailService() {
        return new MockEmailService();
    }

    @Bean
    public ServletRegistrationBean h2ConsoleServletRegistration() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new WebServlet());
        bean.addUrlMappings("/console/*");

        return bean;
    }
}
