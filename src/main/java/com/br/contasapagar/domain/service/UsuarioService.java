package com.br.contasapagar.domain.service;

import com.br.contasapagar.domain.model.Usuario;
import com.br.contasapagar.domain.repository.UsuarioRepository;
import com.br.contasapagar.infrastructure.message.MessageHandler;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.br.contasapagar.shared.constant.Constants.NOT_FOUND;
import static com.br.contasapagar.shared.constant.Constants.USUARIO_EMAIL_EXISTENTE;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final MessageHandler messageHandler;

    public Page<Usuario> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Usuario findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(messageHandler.getMessage(NOT_FOUND, "Usuário")));
    }

    public Usuario save(Usuario usuario){
        validarUsername(usuario.getUsername());

        return repository.save(usuario);
    }

    private void validarUsername(String username) {
        if(repository.existsByUsername(username)) {
            throw new EntityExistsException(messageHandler.getMessage(USUARIO_EMAIL_EXISTENTE, username));
        }
    }
}
