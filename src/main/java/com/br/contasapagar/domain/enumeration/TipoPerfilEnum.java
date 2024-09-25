package com.br.contasapagar.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoPerfilEnum {
    ROLE_ADMIN("Perfil Administrador"),
    ROLE_USUARIO("Perfil Usuário");

    private final String descricao;
}
