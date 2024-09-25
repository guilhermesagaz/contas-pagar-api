package com.br.contasapagar.domain.model;

import com.br.contasapagar.domain.enumeration.SituacaoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal valor;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private SituacaoEnum situacao;

    private LocalDate dataVencimento;

    private LocalDate dataPagamento;

    public void setAtrasada() {
        this.situacao = SituacaoEnum.ATRASADA;
    }
}
