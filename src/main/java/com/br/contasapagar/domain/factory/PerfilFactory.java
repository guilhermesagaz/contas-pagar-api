package com.br.contasapagar.domain.factory;

import com.br.contasapagar.application.payload.perfil.PerfilRequest;
import com.br.contasapagar.application.payload.perfil.PerfilResponse;
import com.br.contasapagar.domain.model.Perfil;
import com.br.contasapagar.domain.service.PermissaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PerfilFactory {

    private final PermissaoService permissaoService;
    private final PermissaoFactory permissaoFactory;

    public Perfil toEntity(PerfilRequest request) {
        return Perfil.builder()
                .tipo(request.getTipo())
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .permissoes(request.getPermissoesIds().stream()
                        .map(permissaoService::findById)
                        .collect(Collectors.toList()))
                .build();
    }

    public PerfilResponse toResponse(Perfil entity) {
        return PerfilResponse.builder()
                .id(entity.getId())
                .tipo(entity.getTipo())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .permissoes(entity.getPermissoes().stream()
                        .map(permissaoFactory::toResponse)
                        .toList())
                .build();
    }
}
