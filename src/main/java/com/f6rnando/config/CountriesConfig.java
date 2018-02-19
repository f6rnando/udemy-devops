package com.f6rnando.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.validation.constraints.NotNull;
import java.util.Map;

/************************************
 Created by f6rnando@gmail.com
 2018-02-19
 Maps the i18n/countries.properties file
 *************************************/

@Configuration
@PropertySource("classpath:i18n/countries.properties")
@ConfigurationProperties(prefix = "country")
public class CountriesConfig {

    @NotNull
    private Map<String, String> key;

    public CountriesConfig() {
    }

    public Map<String, String> getKey() {
        return key;
    }

    public void setKey(Map<String, String> key) {
        this.key = key;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CountriesConfig{");
        sb.append("key=").append(key);
        sb.append('}');
        return sb.toString();
    }
}
