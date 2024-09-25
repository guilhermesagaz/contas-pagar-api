package com.br.contasapagar.domain.factory;

import com.br.contasapagar.application.payload.auth.CustomUserDetails;
import com.br.contasapagar.domain.model.Perfil;
import com.br.contasapagar.domain.model.Usuario;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class AuthFactory {

    public CustomUserDetails toCustomUserDetails(Usuario usuario) {
        return CustomUserDetails.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(getAuthorities(usuario.getPerfis()))
                .accountNonExpired(!usuario.isExpirado())
                .accountNonLocked(!usuario.isBloqueado())
                .credentialsNonExpired(!usuario.isCredenciaisExpiradas())
                .enabled(usuario.isAtivo())
                .build();
    }

    private List<SimpleGrantedAuthority> getAuthorities(List<Perfil> perfisUsuario) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        perfisUsuario.forEach(perfil -> {
            authorities.add(new SimpleGrantedAuthority(perfil.getTipo().name()));

            perfil.getPermissoes()
                    .forEach(permissao -> authorities.add(new SimpleGrantedAuthority(permissao.getNome())));
        });

        return authorities;
    }
}
