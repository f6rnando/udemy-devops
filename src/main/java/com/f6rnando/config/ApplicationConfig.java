package com.f6rnando.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("file:///${user.home}/.f6rnando-devops/application-common.properties")
public class ApplicationConfig {

    @Value("${aws.s3.profile}")
    private String awsProfileName;

    @Bean
    public AmazonS3 s3Client() {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider(awsProfileName))
                .withRegion(Regions.US_WEST_1)
                .build();

        return s3Client;
    }
}
