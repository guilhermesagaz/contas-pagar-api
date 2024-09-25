package com.br.contasapagar.application.payload.usuario;

import com.br.contasapagar.application.payload.perfil.PerfilResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String username;
    private List<PerfilResponse> perfis;
}
