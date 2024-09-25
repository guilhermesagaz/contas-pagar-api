package com.br.contasapagar.infrastructure.configuration.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

public class CustomRabbitTemplate extends RabbitTemplate {

    private MessageConverter customMessageConverter = new SimpleMessageConverter();

    public CustomRabbitTemplate(ConnectionFactory connectionFactory) {
        super.setConnectionFactory(connectionFactory);
    }

    public void customConvertAndSend(String exchange, String routingKey, final Object object) {
        send(exchange, routingKey, customMessageConverter.toMessage(object, new MessageProperties()), null);
    }

    public void customConvertAndSend(String exchange,
                                     String routingKey,
                                     final Object object,
                                     MessageProperties messageProperties) {
        Message message = customMessageConverter.toMessage(object, messageProperties);

        send(exchange, routingKey, message, null);
    }

    public void setCustomMessageConverter(MessageConverter messageConverter) {
        this.customMessageConverter = messageConverter;
    }
}
