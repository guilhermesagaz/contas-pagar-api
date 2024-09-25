package com.br.contasapagar.presentation.controller;

import com.br.contasapagar.application.payload.perfil.PerfilRequest;
import com.br.contasapagar.application.payload.perfil.PerfilResponse;
import com.br.contasapagar.domain.factory.PerfilFactory;
import com.br.contasapagar.domain.service.PerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.br.contasapagar.shared.constant.Constants.ID;

@RequiredArgsConstructor
@RestController
@PreAuthorize("hasAuthority('usuario:admin')")
@RequestMapping("perfil")
public class PerfilController {

    private final PerfilService service;
    private final PerfilFactory factory;

    @GetMapping
    public ResponseEntity<Page<PerfilResponse>> findAllPaged(
            @PageableDefault(sort = ID, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(service.findAll(pageable).map(factory::toResponse));
    }

    @GetMapping("{id}")
    public ResponseEntity<PerfilResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(factory.toResponse(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<PerfilResponse> save(@RequestBody @Valid PerfilRequest request) {
        return ResponseEntity.ok().body(factory.toResponse(service.save(factory.toEntity(request))));
    }
}
