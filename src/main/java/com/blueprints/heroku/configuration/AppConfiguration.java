package com.blueprints.heroku.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public ApplicationInfoProvider getAppInfoProvider() {
        return new ApplicationInfoProvider();
    }
}
