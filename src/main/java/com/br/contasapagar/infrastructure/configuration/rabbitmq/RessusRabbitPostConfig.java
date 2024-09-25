package com.br.contasapagar.infrastructure.configuration.rabbitmq;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@RequiredArgsConstructor
@Configuration
@Profile("!test")
public class RessusRabbitPostConfig {

    private final ConnectionFactory connectionFactory;
    private final RabbitConfig rabbitConfig;
    private final SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory;

    @PostConstruct
    public void configRetryPolicy() {
        simpleRabbitListenerContainerFactory.setAdviceChain(rabbitConfig.retryInterceptor());
    }

    @PostConstruct
    public void initializeResources() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

        rabbitConfig.createExchange(rabbitAdmin);
        rabbitConfig.createQueueDlq(rabbitAdmin);
        rabbitConfig.createQueueContasImportacaoIn(rabbitAdmin);
        rabbitConfig.createQueueContasImportacaoErroIn(rabbitAdmin);
    }
}
