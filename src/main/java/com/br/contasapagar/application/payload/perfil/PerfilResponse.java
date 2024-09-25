package com.br.contasapagar.application.payload.perfil;

import com.br.contasapagar.application.payload.permissao.PermissaoResponse;
import com.br.contasapagar.domain.enumeration.TipoPerfilEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PerfilResponse {

    private Long id;
    private TipoPerfilEnum tipo;
    private String nome;
    private String descricao;
    private List<PermissaoResponse> permissoes;
}
