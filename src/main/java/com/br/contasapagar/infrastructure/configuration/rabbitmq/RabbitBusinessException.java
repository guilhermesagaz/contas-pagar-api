package com.br.contasapagar.infrastructure.configuration.rabbitmq;

public abstract class RabbitBusinessException extends Exception {

    protected RabbitBusinessException(final String message) {
        super(message);
    }
}
