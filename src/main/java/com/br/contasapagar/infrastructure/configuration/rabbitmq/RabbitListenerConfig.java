package com.br.contasapagar.infrastructure.configuration.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Configuration
@EnableRabbit
public class RabbitListenerConfig implements RabbitListenerConfigurer {

    private final Validator validator;

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
        rabbitListenerEndpointRegistrar.setMessageHandlerMethodFactory(validatingHandlerMethodFactory());
    }

    @Bean
    public DefaultMessageHandlerMethodFactory validatingHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory defaultMessageHandlerMethodFactory =
                new DefaultMessageHandlerMethodFactory();

        defaultMessageHandlerMethodFactory.setValidator(validator);

        return defaultMessageHandlerMethodFactory;
    }
}
