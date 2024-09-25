package com.br.contasapagar.infrastructure.consumer;

import com.br.contasapagar.application.dto.ContaDto;
import com.br.contasapagar.domain.factory.ContaFactory;
import com.br.contasapagar.domain.model.EnvioArquivo;
import com.br.contasapagar.domain.service.ContaImportacaoCsvService;
import com.br.contasapagar.domain.service.RabbitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.br.contasapagar.shared.constant.Constants.ENVIO_ARQUIVO_ID;

@Slf4j
@RequiredArgsConstructor
@Component
public class ContaConsumer {

    private final ContaImportacaoCsvService service;
    private final ContaFactory factory;
    private final RabbitService rabbitService;

    @RabbitListener(queues = "${contas-pagar.queue.contas-importacao.in}", concurrency = "${rabbitmq.queue.concurrency}")
    public void pocessarImportacaoContas(@Payload Long envioArquivoId, Message<?> message) {
        try {
            EnvioArquivo envioArquivo = service.findEnvioArquivo(envioArquivoId);

            List<ContaDto> contaDtos = service.lerValidarArquivo(envioArquivo);

            service.salvarDados(factory.toEntityList(contaDtos));
            service.setConcluidoEnvioArquivo(envioArquivo);
        } catch (Exception exception) {
            log.info("Erro ao consumir fila contas-pagar.queue.contas-importacao.in:: {}", exception.getMessage());
            rabbitService.enviarContasImportacaoErro(exception.getMessage(), envioArquivoId);

            throw exception;
        }
    }

    @RabbitListener(queues = "${contas-pagar.queue.contas-importacao-erro.in}", concurrency = "${rabbitmq.queue.concurrency}")
    public void setErroAoImpotarContas(@Payload String mensagemErro, Message<?> message) {
        try {
            Long envioArquivoId = getEnvioArquivoId(message);
            service.setErroEnvioArquivo(envioArquivoId, mensagemErro);
        } catch (Exception exception) {
            log.info("Erro ao consumir fila contas-pagar.queue.contas-importacao-erro.in:: {}", exception.getMessage());
            throw exception;
        }
    }

    private Long getEnvioArquivoId(Message<?> message) {
        return (Long) message
                .getHeaders()
                .get(ENVIO_ARQUIVO_ID);
    }
}
