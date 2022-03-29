package com.blueprints.heroku.services.eventstream;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    public Queue ordersProcessingQueue() {
        return new Queue(EventStreamProcessor.TOPIC_ORDER_PROCESS);
    }
}
