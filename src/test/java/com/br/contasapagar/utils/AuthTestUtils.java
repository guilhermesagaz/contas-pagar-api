package com.br.contasapagar.utils;

import com.br.contasapagar.application.payload.auth.CustomUserDetails;
import com.br.contasapagar.infrastructure.properties.AuthenticationProperties;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static com.br.contasapagar.utils.PerfilTestUtils.PERFIL_TIPO;
import static com.br.contasapagar.utils.PermissaoTestUtils.PERMISSAO_NOME;
import static com.br.contasapagar.utils.UsuarioTestUtils.*;

public class AuthTestUtils {

    public static final String SECRET = "fk4OfhWAAJzHigUhGUf2DArEujQyAp==";
    public static final Integer EXPIRATION_ACCESS = 1800;
    public static final Integer EXPIRATION_REFRESH = 86400;

    public static CustomUserDetails createCustomUserDetails() {
        return CustomUserDetails.builder()
                .id(USUARIO_ID)
                .nome(USUARIO_NOME)
                .username(USUARIO_USERNAME)
                .password(USUARIO_PASSWORD)
                .authorities(List.of(new SimpleGrantedAuthority(PERFIL_TIPO.name()),
                                new SimpleGrantedAuthority(PERMISSAO_NOME)))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
    }

    public static AuthenticationProperties createAuthenticationProperties() {
        AuthenticationProperties.TokenDetails accessToken = new AuthenticationProperties.TokenDetails();
        accessToken.setExpirationInSeconds(EXPIRATION_ACCESS);
        AuthenticationProperties.TokenDetails refreshToken = new AuthenticationProperties.TokenDetails();
        refreshToken.setExpirationInSeconds(EXPIRATION_REFRESH);

        AuthenticationProperties authenticationProperties = new AuthenticationProperties();
        authenticationProperties.setSecretKey(SECRET);
        authenticationProperties.setAccessToken(accessToken);
        authenticationProperties.setRefreshToken(refreshToken);

        return authenticationProperties;
    }
}
