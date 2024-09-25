package com.br.contasapagar.domain.factory;

import com.br.contasapagar.application.payload.usuario.UsuarioRequest;
import com.br.contasapagar.application.payload.usuario.UsuarioResponse;
import com.br.contasapagar.domain.model.Usuario;
import com.br.contasapagar.domain.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UsuarioFactory {

    private final PerfilService perfilService;
    private final PerfilFactory perfilFactory;
    private final PasswordEncoder passwordEncoder;

    public Usuario toEntity(UsuarioRequest request) {
        return Usuario.builder()
                .nome(request.getNome())
                .username(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .perfis(request.getPerfisIds().stream()
                        .map(perfilService::findById)
                        .collect(Collectors.toList()))
                .expirado(false)
                .bloqueado(false)
                .credenciaisExpiradas(false)
                .ativo(true)
                .build();
    }

    public UsuarioResponse toResponse(Usuario entity) {
        return UsuarioResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .username(entity.getUsername())
                .perfis(entity.getPerfis().stream()
                        .map(perfilFactory::toResponse)
                        .toList())
                .build();
    }
}
