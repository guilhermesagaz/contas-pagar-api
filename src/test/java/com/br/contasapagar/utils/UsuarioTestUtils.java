package com.br.contasapagar.utils;

import com.br.contasapagar.application.payload.usuario.UsuarioRequest;
import com.br.contasapagar.application.payload.usuario.UsuarioResponse;
import com.br.contasapagar.domain.model.Usuario;

import java.util.List;

import static com.br.contasapagar.utils.PerfilTestUtils.*;

public class UsuarioTestUtils {

    public static final Long USUARIO_ID = 1L;
    public static final String USUARIO_NOME = "Guilherme Sagaz";
    public static final String USUARIO_USERNAME = "guilherme.sagaz.p@gmail.com";
    public static final String USUARIO_PASSWORD = "admin123";

    public static Usuario createUsuario() {
        return Usuario.builder()
                .id(USUARIO_ID)
                .nome(USUARIO_NOME)
                .username(USUARIO_USERNAME)
                .password(USUARIO_PASSWORD)
                .expirado(false)
                .bloqueado(false)
                .credenciaisExpiradas(false)
                .ativo(true)
                .perfis(List.of(createPerfil()))
                .build();
    }

    public static UsuarioRequest createUsuarioRequest() {
        return UsuarioRequest.builder()
                .nome(USUARIO_NOME)
                .email(USUARIO_USERNAME)
                .password(USUARIO_PASSWORD)
                .perfisIds(List.of(PERFIL_ID))
                .build();
    }

    public static UsuarioResponse createUsuarioResponse() {
        return UsuarioResponse.builder()
                .id(USUARIO_ID)
                .nome(USUARIO_NOME)
                .username(USUARIO_USERNAME)
                .perfis(List.of(createPerfilResponse()))
                .build();
    }
}
