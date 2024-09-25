package com.br.contasapagar.domain.service;

import com.br.contasapagar.domain.model.Permissao;
import com.br.contasapagar.domain.repository.PermissaoRepository;
import com.br.contasapagar.application.exception.NotFoundException;
import com.br.contasapagar.infrastructure.message.MessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.br.contasapagar.shared.constant.Constants.NOT_FOUND;

@RequiredArgsConstructor
@Service
public class PermissaoService {

    private final PermissaoRepository repository;
    private final MessageHandler messageHandler;

    public Page<Permissao> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Permissao findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageHandler.getMessage(NOT_FOUND, "Permissão")));
    }
}
