package com.br.contasapagar.utils;

import com.br.contasapagar.application.payload.conta.ContaRequest;
import com.br.contasapagar.application.payload.conta.ContaResponse;
import com.br.contasapagar.application.payload.conta.SituacaoRequest;
import com.br.contasapagar.application.payload.conta.ValorTotalResponse;
import com.br.contasapagar.domain.enumeration.SituacaoEnum;
import com.br.contasapagar.domain.model.Conta;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContaTestUtils {

    public static final Long CONTA_ID = 1L;
    public static final BigDecimal CONTA_VALOR = BigDecimal.valueOf(150.50);
    public static final String CONTA_DESCRICAO = "Conta de teste";
    public static final SituacaoEnum CONTA_SITUACAO = SituacaoEnum.PENDENTE;
    public static final LocalDate CONTA_DATA_VENCIMENTO = LocalDate.of(2024, 9, 30);

    public static Conta createConta() {
        return Conta.builder()
                .id(CONTA_ID)
                .valor(CONTA_VALOR)
                .descricao(CONTA_DESCRICAO)
                .situacao(CONTA_SITUACAO)
                .dataVencimento(CONTA_DATA_VENCIMENTO)
                .dataPagamento(null)
                .build();
    }

    public static ContaRequest createContaRequest() {
        return ContaRequest.builder()
                .valor(CONTA_VALOR)
                .descricao(CONTA_DESCRICAO)
                .dataVencimento(CONTA_DATA_VENCIMENTO)
                .build();
    }

    public static SituacaoRequest createSituacaoRequest() {
        return SituacaoRequest.builder()
                .situacao(SituacaoEnum.PAGO)
                .build();
    }

    public static ContaResponse createContaResponse() {
        return ContaResponse.builder()
                .id(CONTA_ID)
                .valor(CONTA_VALOR)
                .descricao(CONTA_DESCRICAO)
                .situacao(CONTA_SITUACAO)
                .dataVencimento(CONTA_DATA_VENCIMENTO)
                .dataPagamento(null)
                .build();
    }

    public static ValorTotalResponse createValorTotalResponse() {
        return ValorTotalResponse.builder()
                .valorTotal(CONTA_VALOR)
                .build();
    }
}
