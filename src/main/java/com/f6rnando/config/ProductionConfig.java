package com.f6rnando.config;

import com.f6rnando.backend.service.EmailService;
import com.f6rnando.backend.service.SmtpEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/************************************
 Created by f6rnando@gmail.com
 2018-02-02
 *************************************/

@Configuration
@Profile("prod")
@PropertySource("file:///${user.home}/.f6rnando-devops/application-prod.properties")
public class ProductionConfig {

    @Bean
    public EmailService emailService() {
        return new SmtpEmailService();
    }
}
