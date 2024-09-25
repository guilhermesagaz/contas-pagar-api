package com.br.contasapagar.domain.factory;

import com.br.contasapagar.application.payload.permissao.PermissaoResponse;
import com.br.contasapagar.domain.model.Permissao;
import org.springframework.stereotype.Component;

@Component
public class PermissaoFactory {

    public PermissaoResponse toResponse(Permissao entity) {
        return PermissaoResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .build();
    }
}
