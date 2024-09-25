package com.br.contasapagar.utils;

import com.br.contasapagar.application.payload.permissao.PermissaoResponse;
import com.br.contasapagar.domain.model.Permissao;

public class PermissaoTestUtils {

    public static final Long PERMISSAO_ID = 1L;
    public static final String PERMISSAO_NOME = "usuario:admin";
    public static final String PERMISSAO_DESCRICAO = "Permissão para usuários administrador";

    public static Permissao createPermissao() {
        return Permissao.builder()
                .id(PERMISSAO_ID)
                .nome(PERMISSAO_NOME)
                .descricao(PERMISSAO_DESCRICAO)
                .build();
    }

    public static PermissaoResponse createPermissaoResponse() {
        return PermissaoResponse.builder()
                .id(PERMISSAO_ID)
                .nome(PERMISSAO_NOME)
                .descricao(PERMISSAO_DESCRICAO)
                .build();
    }
}
