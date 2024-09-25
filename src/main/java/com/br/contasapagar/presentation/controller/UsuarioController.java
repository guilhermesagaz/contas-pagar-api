package com.br.contasapagar.presentation.controller;

import com.br.contasapagar.application.payload.usuario.UsuarioRequest;
import com.br.contasapagar.application.payload.usuario.UsuarioResponse;
import com.br.contasapagar.domain.factory.UsuarioFactory;
import com.br.contasapagar.domain.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.br.contasapagar.shared.constant.Constants.ID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("usuario")
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioFactory factory;

    @PostMapping
    public ResponseEntity<UsuarioResponse> save(@RequestBody @Valid UsuarioRequest request) {
        log.info("Solicitação de registro de usuário recebida: {}", request);

        return ResponseEntity.ok().body(factory.toResponse(service.save(factory.toEntity(request))));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('usuario:admin')")
    public ResponseEntity<Page<UsuarioResponse>> findAll(
            @PageableDefault(sort = ID, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(service.findAll(pageable).map(factory::toResponse));
    }
}
