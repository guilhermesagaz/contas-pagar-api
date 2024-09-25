package com.br.contasapagar.domain.service;

import com.br.contasapagar.application.exception.TokenInvalidException;
import com.br.contasapagar.application.payload.auth.CustomUserDetails;
import com.br.contasapagar.domain.enumeration.TokenEnum;
import com.br.contasapagar.domain.model.Token;
import com.br.contasapagar.domain.model.Usuario;
import com.br.contasapagar.domain.repository.TokenRepository;
import com.br.contasapagar.infrastructure.message.MessageHandler;
import com.br.contasapagar.infrastructure.properties.AuthenticationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.br.contasapagar.utils.AuthTestUtils.*;
import static com.br.contasapagar.utils.UsuarioTestUtils.createUsuario;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TokenServiceTest {

    private static final String TOKEN = "jwt-token";

    @InjectMocks
    private TokenService service;

    @Mock
    private TokenRepository repository;

    @Mock
    private AuthenticationProperties authenticationProperties;

    @Mock
    private MessageHandler messageHandler;

    @Test
    public void testSaveTokenSuccess() {
        Usuario usuario = createUsuario();
        TokenEnum tipo = TokenEnum.ACCESS_TOKEN;

        Token expectedToken = Token.builder()
                .token(TOKEN)
                .usuario(usuario)
                .tipo(tipo)
                .revogado(false)
                .build();

        when(repository.save(any())).thenReturn(expectedToken);

        Token result = service.save(usuario, TOKEN, tipo);

        assertNotNull(result);
        assertEquals(expectedToken, result);
        verify(repository, times(1)).save(any());
    }

    @Test
    public void testRevogarTokenUsuarioSuccess() {
        Usuario usuario = createUsuario();
        Token token = Token.builder()
                .token(TOKEN)
                .usuario(usuario)
                .tipo(TokenEnum.ACCESS_TOKEN)
                .revogado(false)
                .build();

        when(repository.findByUsuarioAndTipoIn(any(), any())).thenReturn(List.of(token));

        service.revogarTokenUsuario(usuario, List.of(TokenEnum.ACCESS_TOKEN));

        verify(repository, times(1)).findByUsuarioAndTipoIn(any(), any());
        verify(repository, times(1)).saveAll(List.of(token));
    }

    @Test
    public void testGenerateAccessTokenSuccess() {
        CustomUserDetails userDetails = createCustomUserDetails();
        AuthenticationProperties accessTokenProperties = createAuthenticationProperties();

        when(authenticationProperties.getAccessToken()).thenReturn(accessTokenProperties.getAccessToken());
        when(authenticationProperties.getSecretKey()).thenReturn(accessTokenProperties.getSecretKey());

        String jwtToken = service.generateAccessToken(userDetails);

        assertNotNull(jwtToken);
        assertFalse(jwtToken.isEmpty());
    }

    @Test
    public void testGenerateRefreshTokenSuccess() {
        CustomUserDetails userDetails = createCustomUserDetails();
        AuthenticationProperties accessTokenProperties = createAuthenticationProperties();

        when(authenticationProperties.getRefreshToken()).thenReturn(accessTokenProperties.getRefreshToken());
        when(authenticationProperties.getSecretKey()).thenReturn(accessTokenProperties.getSecretKey());

        String refreshToken = service.generateRefreshToken(userDetails);

        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
    }

    @Test
    public void testValidateTokenInvalidToken() {
        when(repository.findByTokenAndRevogado(TOKEN, false)).thenReturn(Optional.empty());
        when(messageHandler.getMessage(any())).thenReturn("Token inválido");

        assertThrows(TokenInvalidException.class, () -> service.validateToken(TOKEN));

        verify(repository, times(1)).findByTokenAndRevogado(TOKEN, false);
    }
}
