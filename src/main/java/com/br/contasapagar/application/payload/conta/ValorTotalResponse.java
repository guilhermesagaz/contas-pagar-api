package com.br.contasapagar.application.payload.conta;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ValorTotalResponse {

    private BigDecimal valorTotal;
}
