package com.br.contasapagar.presentation.controller;

import com.br.contasapagar.application.payload.permissao.PermissaoResponse;
import com.br.contasapagar.domain.factory.PermissaoFactory;
import com.br.contasapagar.domain.service.PermissaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.br.contasapagar.shared.constant.Constants.ID;

@RequiredArgsConstructor
@RestController
@PreAuthorize("hasAuthority('usuario:admin')")
@RequestMapping("permissao")
public class PermissaoController {

    private final PermissaoService service;
    private final PermissaoFactory factory;

    @GetMapping
    public ResponseEntity<Page<PermissaoResponse>> findAllPaged(
            @PageableDefault(sort = ID, direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok().body(service.findAll(pageable).map(factory::toResponse));
    }

    @GetMapping("{id}")
    public ResponseEntity<PermissaoResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(factory.toResponse(service.findById(id)));
    }
}
