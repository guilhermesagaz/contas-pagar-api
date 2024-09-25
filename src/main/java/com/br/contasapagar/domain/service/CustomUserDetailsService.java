package com.br.contasapagar.domain.service;

import com.br.contasapagar.application.payload.auth.CustomUserDetails;
import com.br.contasapagar.domain.model.Usuario;
import com.br.contasapagar.domain.factory.AuthFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;
    private final AuthFactory authFactory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.findByUsername(username);
        CustomUserDetails customUserDetails = authFactory.toCustomUserDetails(usuario);

        try {
            new AccountStatusUserDetailsChecker().check(customUserDetails);
        } catch (AccountStatusException e) {
            log.error("Não foi possível autenticar o usuário.", e);
            throw new RuntimeException(e.getMessage());
        }

        return customUserDetails;
    }
}
