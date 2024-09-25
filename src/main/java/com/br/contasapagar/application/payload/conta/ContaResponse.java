package com.br.contasapagar.application.payload.conta;

import com.br.contasapagar.domain.enumeration.SituacaoEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ContaResponse {

    private Long id;
    private BigDecimal valor;
    private String descricao;
    private SituacaoEnum situacao;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
}
