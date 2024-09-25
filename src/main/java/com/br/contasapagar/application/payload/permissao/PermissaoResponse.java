package com.br.contasapagar.application.payload.permissao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PermissaoResponse {

    private Long id;
    private String nome;
    private String descricao;
}
