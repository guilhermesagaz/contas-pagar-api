package com.br.contasapagar.domain.service;

import com.br.contasapagar.domain.model.Conta;
import com.br.contasapagar.domain.enumeration.SituacaoEnum;
import com.br.contasapagar.domain.repository.ContaRepository;
import com.br.contasapagar.application.exception.NotFoundException;
import com.br.contasapagar.infrastructure.message.MessageHandler;
import com.br.contasapagar.presentation.exception.ContaException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.br.contasapagar.shared.constant.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContaService {

    private final ContaRepository repository;
    private final MessageHandler messageHandler;

    public Conta findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(messageHandler.getMessage(NOT_FOUND, "Conta")));
    }

    public Page<Conta> findAllPaged(String descricao, LocalDate dataVencimento, Pageable pageable) {
        return repository.findAllPaged(descricao, dataVencimento, pageable);
    }

    public BigDecimal sumTotalPagoPorPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        validarPeriodo(dataInicial, dataFinal);

        return repository.sumValorByDataPagamentoBetween(dataInicial, dataFinal);
    }

    public Conta save(Conta conta) {
        return repository.save(conta);
    }

    public Conta update(Long id, Conta toUpdate) {
        Conta conta = findById(id);

        validarSituacao(conta);

        toUpdate.setId(conta.getId());
        toUpdate.setSituacao(SituacaoEnum.CANCELADO.equals(conta.getSituacao())
                ? conta.getSituacao() : toUpdate.getSituacao());

        return repository.save(toUpdate);
    }

    public Conta updateSituacao(Long id, Conta toUpdate) {
        Conta conta = findById(id);

        validarSituacao(conta);

        conta.setSituacao(toUpdate.getSituacao());
        conta.setDataPagamento(toUpdate.getDataPagamento());

        validarAlteracaoSituacao(conta);

        return repository.save(conta);
    }

    @Scheduled(cron = "${conta.contas-atrasadas.schedule-cron}")
    public void alterarSituacaoContasAtrasadas() {
        log.info("Buscando contas com data de vencimento no dia enterior e alterando situação.");

        List<Conta> contasAtrasadas = repository.findAllAtrasadosNaData(LocalDate.now());
        contasAtrasadas.forEach(Conta::setAtrasada);

        if (!contasAtrasadas.isEmpty()) {
            repository.saveAll(contasAtrasadas);
        }
    }

    private void validarSituacao(Conta conta) {
        if (SituacaoEnum.PAGO.equals(conta.getSituacao())) {
            throw new ContaException(messageHandler.getMessage(CONTA_SITUACAO_PENDENTE_EXCEPTION,
                    conta.getSituacao().name()));
        }
    }

    private void validarAlteracaoSituacao(Conta conta) {
        if (SituacaoEnum.PENDENTE.equals(conta.getSituacao()) && conta.getDataVencimento().isBefore(LocalDate.now())) {
            throw new ContaException(messageHandler.getMessage(CONTA_SITUACAO_EXCEPTION, conta.getSituacao().name()));
        }

        if (!List.of(SituacaoEnum.PAGO, SituacaoEnum.CANCELADO, SituacaoEnum.PENDENTE).contains(conta.getSituacao())) {
            throw new ContaException(messageHandler.getMessage(CONTA_SITUACAO_EXCEPTION, conta.getSituacao().name()));
        }
    }

    private void validarPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        if (dataInicial.isAfter(dataFinal)) {
            throw new ContaException(messageHandler.getMessage(CONTA_PERIODO_EXCEPTION));
        }
    }
}
