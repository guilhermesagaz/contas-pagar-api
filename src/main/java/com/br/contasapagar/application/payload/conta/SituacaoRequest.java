package com.br.contasapagar.application.payload.conta;

import com.br.contasapagar.domain.enumeration.SituacaoEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SituacaoRequest {

    @NotNull(message = "O campo situação é obrigatório e deve ser informado")
    private SituacaoEnum situacao;
}
