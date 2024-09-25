package com.br.contasapagar.domain.service;

import com.br.contasapagar.infrastructure.configuration.rabbitmq.CustomRabbitTemplate;
import com.br.contasapagar.infrastructure.configuration.rabbitmq.QueueProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.stereotype.Component;

import static com.br.contasapagar.shared.constant.Constants.ENVIO_ARQUIVO_ID;

@RequiredArgsConstructor
@Component
public class RabbitService {

    private final CustomRabbitTemplate rabbitTemplate;
    private final QueueProperties queueProperties;

    public void enviarContasImportacao(Long envioArquivoId) {
        MessageProperties props = MessagePropertiesBuilder.newInstance().build();

        rabbitTemplate.customConvertAndSend(
                queueProperties.getExchange(),
                queueProperties.getRoutingKeyContasImportacaoIn(),
                envioArquivoId,
                props);
    }

    public void enviarContasImportacaoErro(String mensagemErro, Long envioArquivoId) {
        MessageProperties props = MessagePropertiesBuilder.newInstance().build();
        props.setHeader(ENVIO_ARQUIVO_ID, envioArquivoId);

        rabbitTemplate.customConvertAndSend(
                queueProperties.getExchange(),
                queueProperties.getRoutingKeyContasImportacaoErroIn(),
                mensagemErro,
                props);
    }
}
