package com.br.contasapagar.infrastructure.configuration.rabbitmq;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class QueueProperties {
    @Value("${contas-pagar.exchange}")
    private String exchange;

    @Value("${contas-pagar.queue.dlq}")
    private String queueDlq;

    @Value("${contas-pagar.routing.key.dlq}")
    private String routingKeyDlq;

    @Value("${contas-pagar.queue.contas-importacao.in}")
    private String contasImportacaoIn;

    @Value("${contas-pagar.routing.key.contas-importacao.in}")
    private String routingKeyContasImportacaoIn;

    @Value("${contas-pagar.queue.contas-importacao-erro.in}")
    private String contasImportacaoErroIn;

    @Value("${contas-pagar.routing.key.contas-importacao-erro.in}")
    private String routingKeyContasImportacaoErroIn;
}
