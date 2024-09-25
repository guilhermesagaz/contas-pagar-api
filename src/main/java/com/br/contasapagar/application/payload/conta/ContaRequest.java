package com.br.contasapagar.application.payload.conta;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaRequest {

    @NotNull(message = "O campo valor é obrigatório e deve ser informado")
    @Digits(integer = 15, fraction = 2, message = "O campo valor deve ter no máximo 15 dígitos e 2 casas decimais")
    private BigDecimal valor;

    @Size(max = 400, message = "O tamanho do campo descrição deve ser menor ou igual à {max}")
    private String descricao;

    @NotNull(message = "O campo data de vencimento é obrigatório e deve ser informado")
    private LocalDate dataVencimento;
}
