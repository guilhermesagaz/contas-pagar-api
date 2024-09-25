package com.br.contasapagar.utils;

import com.br.contasapagar.application.payload.perfil.PerfilRequest;
import com.br.contasapagar.application.payload.perfil.PerfilResponse;
import com.br.contasapagar.domain.enumeration.TipoPerfilEnum;
import com.br.contasapagar.domain.model.Perfil;

import java.util.List;

import static com.br.contasapagar.utils.PermissaoTestUtils.createPermissao;
import static com.br.contasapagar.utils.PermissaoTestUtils.createPermissaoResponse;

public class PerfilTestUtils {

    public static final Long PERFIL_ID = 1L;
    public static final TipoPerfilEnum PERFIL_TIPO = TipoPerfilEnum.ROLE_ADMIN;
    public static final String PERFIL_NOME = "Administrador";
    public static final String PERFIL_DESCRICAO = "Perfil para usuários que realizam funções administrativas no aplicativo.";

    public static Perfil createPerfil() {
        return Perfil.builder()
                .id(PERFIL_ID)
                .tipo(PERFIL_TIPO)
                .nome(PERFIL_NOME)
                .descricao(PERFIL_DESCRICAO)
                .permissoes(List.of(createPermissao()))
                .build();
    }

    public static PerfilRequest createPerfilRequest() {
        return PerfilRequest.builder()
                .tipo(PERFIL_TIPO)
                .nome(PERFIL_NOME)
                .descricao(PERFIL_DESCRICAO)
                .permissoesIds(List.of(1L))
                .build();
    }

    public static PerfilResponse createPerfilResponse() {
        return PerfilResponse.builder()
                .id(PERFIL_ID)
                .tipo(PERFIL_TIPO)
                .nome(PERFIL_NOME)
                .descricao(PERFIL_DESCRICAO)
                .permissoes(List.of(createPermissaoResponse()))
                .build();
    }
}
