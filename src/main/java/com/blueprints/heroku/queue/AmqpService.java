package com.blueprints.heroku.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AmqpService {
    private Logger logger = LoggerFactory.getLogger(AmqpService.class);
    private final AmqpAdmin amqpAdmin;
    private final AmqpTemplate amqpTemplate;

    public AmqpService(AmqpAdmin amqpAdmin, AmqpTemplate amqpTemplate) {
        this.amqpAdmin = amqpAdmin;
        this.amqpTemplate = amqpTemplate;
    }

    public void send(String queue, String message) {
        amqpTemplate.convertAndSend(queue, message);
    }

}
