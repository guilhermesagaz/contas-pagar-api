package com.br.contasapagar.domain.service;

import com.br.contasapagar.domain.model.Perfil;
import com.br.contasapagar.domain.repository.PerfilRepository;
import com.br.contasapagar.application.exception.NotFoundException;
import com.br.contasapagar.infrastructure.message.MessageHandler;
import com.br.contasapagar.presentation.exception.NomePerfilExistenteException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.br.contasapagar.shared.constant.Constants.NOT_FOUND;
import static com.br.contasapagar.shared.constant.Constants.PERFIL_NOME_EXISTENTE;

@RequiredArgsConstructor
@Service
public class PerfilService {

    private final PerfilRepository repository;
    private final MessageHandler messageHandler;

    public Page<Perfil> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Perfil findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageHandler.getMessage(NOT_FOUND, "Perfil")));
    }

    @Transactional
    public Perfil save(Perfil entity) {
        validarSeExisteNomePerfil(entity.getNome());

        return repository.save(entity);
    }

    private void validarSeExisteNomePerfil(String nome) {
        if (repository.existsByNome(nome)) {
            throw new NomePerfilExistenteException(messageHandler.getMessage(PERFIL_NOME_EXISTENTE, nome));
        }
    }
}
