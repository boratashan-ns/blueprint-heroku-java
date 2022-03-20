package com.blueprints.heroku.configuration;

import com.blueprints.heroku.commons.*;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfiguration {

    @Bean
    public ApplicationInfoProvider getAppInfoProvider() {
        return new ApplicationInfoProvider();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public NewstoreRestClient getRestClient() {
        NwsContext context = NwsContext.Builder
                .start()
                .withCredentials(new Credential("axelarigato.p.api@newstore.com", "Api@2021Mun$2022"))
                .withTenant("axelarigato")
                .withEnvironment(NwsEnvironment.PRODUCTION)
                .withNwsUrl("newstore.net")
                .build();
        return new DefaultRestClient(context);
    }

    @Bean
    public Queue ordersProcessingQueue() {
        return new Queue("orders.req.process");
    }

}
