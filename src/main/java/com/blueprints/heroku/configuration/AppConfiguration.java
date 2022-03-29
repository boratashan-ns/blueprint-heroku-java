package com.blueprints.heroku.configuration;

import com.blueprints.heroku.commons.*;
import com.blueprints.heroku.commons.Credential;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfiguration {
    @Value("${appinfo.credentials.username}")
    private String credentialsUserName;
    @Value("${appinfo.credentials.password}")
    private String credentialsPassword;
    @Value("${appinfo.credentials.endpoint}")
    private String credentialsEndpoint;
    @Value("${appinfo.credentials.tenant}")
    private String credentialsTenant;
    @Value("${appinfo.credentials.environment}")
    private String credentialsEnvironment;
    private String appname;
    @Bean
    public ApplicationInfoProvider getAppInfoProvider() {
        return new ApplicationInfoProvider();
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public NewstoreRestClient getRestClient() {
        /*NwsContext context = NwsContext.Builder
                .start()
                .withCredentials(new Credential("axelarigato.p.api@newstore.com", "Api@2021Mun$2022"))
                .withTenant("axelarigato")
                .withEnvironment(NwsEnvironment.PRODUCTION)
                .withNwsUrl("newstore.net")
                .build(); */
        NwsContext context = NwsContext.Builder
                .start()
                .withCredentials(new Credential(credentialsUserName, credentialsPassword))
                .withTenant(credentialsTenant)
                .withEnvironment(NwsEnvironment.fromString(credentialsEnvironment))
                .withNwsUrl(credentialsEndpoint)
                .build();
        return new DefaultRestClient(context);
    }





}
