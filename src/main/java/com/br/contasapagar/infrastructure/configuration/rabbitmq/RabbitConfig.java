package com.br.contasapagar.infrastructure.configuration.rabbitmq;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.SimpleRetryPolicy;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class RabbitConfig {

    private static final int MAX_RETRY_ATTEMPTS = 3;

    private final QueueProperties queueProperties;
    private final ConnectionFactory connectionFactory;

    private static void createQueue(RabbitAdmin rabbitAdmin, Queue queue, Binding binding) {
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(binding);
    }

    private static Queue createQueue(String queueName) {
        return QueueBuilder.durable(queueName).lazy().build();
    }

    private void createQueue(RabbitAdmin rabbitAdmin, String queueName, String routingKey) {
        createQueue(rabbitAdmin, createQueue(queueName), createBinding(queueName, routingKey));
    }

    private static SimpleRetryPolicy createSimpleRetryPolicy() {
        Map<Class<? extends Throwable>, Boolean> exceptionMap = new HashMap<>();

        exceptionMap.put(RabbitBusinessException.class, true);

        return new SimpleRetryPolicy(MAX_RETRY_ATTEMPTS, exceptionMap, true);
    }

    @Bean
    public CustomRabbitTemplate createCustomRabbitTemplate(final ConnectionFactory connectionFactoryCustom) {
        CustomRabbitTemplate customRabbitTemplate = new CustomRabbitTemplate(connectionFactoryCustom);
        customRabbitTemplate.setCustomMessageConverter(messageConverter());

        return customRabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper());
    }

    @Bean
    public RabbitAdmin criaRabbitAdmin(ConnectionFactory conn) {
        return new RabbitAdmin(conn);
    }

    public void createExchange(RabbitAdmin rabbitAdmin) {
        Exchange exchange = ExchangeBuilder.directExchange(queueProperties.getExchange())
                .durable(Boolean.TRUE)
                .build();

        rabbitAdmin.declareExchange(exchange);
    }

    public void createQueueDlq(RabbitAdmin rabbitAdmin) {
        createQueue(rabbitAdmin, queueProperties.getQueueDlq(), queueProperties.getRoutingKeyDlq());
    }

    public void createQueueContasImportacaoIn(RabbitAdmin rabbitAdmin) {
        Queue queue = QueueBuilder.durable(queueProperties.getContasImportacaoIn())
                .deadLetterExchange(queueProperties.getExchange())
                .deadLetterRoutingKey(queueProperties.getRoutingKeyContasImportacaoIn())
                .lazy()
                .build();

        createQueue(rabbitAdmin, queue,
                createBinding(queueProperties.getContasImportacaoIn(),
                        queueProperties.getRoutingKeyContasImportacaoIn()));
    }

    public void createQueueContasImportacaoErroIn(RabbitAdmin rabbitAdmin) {
        Queue queue = QueueBuilder.durable(queueProperties.getContasImportacaoErroIn())
                .deadLetterExchange(queueProperties.getExchange())
                .deadLetterRoutingKey(queueProperties.getRoutingKeyContasImportacaoErroIn())
                .lazy()
                .build();

        createQueue(rabbitAdmin, queue,
                createBinding(queueProperties.getContasImportacaoErroIn(),
                        queueProperties.getRoutingKeyContasImportacaoErroIn()));
    }

    public JsonMapper objectMapper() {
        return JsonMapper.builder()
                .disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .addModule(new JavaTimeModule())
                .build();
    }

    public Advice retryInterceptor() {
        return RetryInterceptorBuilder
                .stateless()
                .retryPolicy(createSimpleRetryPolicy())
                .recoverer(new RepublishMessageRecoverer(
                        createRabbitErrorTemplate(),
                        queueProperties.getExchange(),
                        queueProperties.getRoutingKeyDlq()))
                .build();
    }

    private Binding createBinding(String queueName, String routingKey) {
        return new Binding(queueName, Binding.DestinationType.QUEUE, queueProperties.getExchange(), routingKey, null);
    }

    private RabbitTemplate createRabbitErrorTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());

        return rabbitTemplate;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactoryMail) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactoryMail);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        return rabbitTemplate;
    }
}
