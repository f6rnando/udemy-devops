package com.f6rnando.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/************************************
 Created by f6rnando@gmail.com
 2018-02-08

 All global Spring configurations
 *************************************/

@Configuration
@EnableJpaRepositories(basePackages = "com.f6rnando.backend.persistence.repositories")
@EntityScan(basePackages = "com.f6rnando.backend.persistence.domain.backend")
@EnableTransactionManagement
public class ApplicationConfig {
}
