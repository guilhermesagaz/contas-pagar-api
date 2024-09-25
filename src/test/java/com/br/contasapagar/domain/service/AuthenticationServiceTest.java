package com.br.contasapagar.domain.service;

import com.br.contasapagar.application.payload.auth.AuthenticationResponse;
import com.br.contasapagar.application.payload.auth.CustomUserDetails;
import com.br.contasapagar.domain.enumeration.TokenEnum;
import com.br.contasapagar.domain.factory.AuthFactory;
import com.br.contasapagar.domain.model.Usuario;
import com.br.contasapagar.infrastructure.properties.AuthenticationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static com.br.contasapagar.utils.AuthTestUtils.createAuthenticationProperties;
import static com.br.contasapagar.utils.AuthTestUtils.createCustomUserDetails;
import static com.br.contasapagar.utils.UsuarioTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService service;

    @Mock
    private AuthFactory authFactory;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthenticationProperties authenticationProperties;

    @Mock
    private TokenService tokenService;

    @Mock
    private UsuarioService usuarioService;

    private Usuario mockUsuario;
    private CustomUserDetails mockUserDetails;
    private AuthenticationProperties tokenProperties;
    private String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJ2ZXJpZmljYWRvIjpmYWxzZSwiYXRpdm8iOnRydWUsIm5vbWUiOiJHdWlsaGVybWUgU2FnYXoiLCJpZCI6MSwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOLHVzdWFyaW86YWRtaW4iLCJ1c2VybmFtZSI6Imd1aWxoZXJtZS5zYWdhei5wQGdtYWlsLmNvbSIsImlhdCI6MTcyNzIyOTk3NSwiaXNzIjoiY29udGFzLXBhZ2FyIiwic3ViIjoiZ3VpbGhlcm1lLnNhZ2F6LnBAZ21haWwuY29tIiwiZXhwIjoxNzI3MjMxNzc1fQ.UclkF180XY7CrZPUuL82Ua_8_35jgCqOhyW2B5zjvNE";
    private String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJ2ZXJpZmljYWRvIjpmYWxzZSwiYXRpdm8iOnRydWUsIm5vbWUiOiJHdWlsaGVybWUgU2FnYXoiLCJpZCI6MSwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOLHVzdWFyaW86YWRtaW4iLCJ1c2VybmFtZSI6Imd1aWxoZXJtZS5zYWdhei5wQGdtYWlsLmNvbSIsImlhdCI6MTcyNzIyOTk3NSwiaXNzIjoiY29udGFzLXBhZ2FyIiwic3ViIjoiZ3VpbGhlcm1lLnNhZ2F6LnBAZ21haWwuY29tIiwiZXhwIjoxNzI3MjMxNzc1fQ.UclkF180XY7CrZPUuL82Ua_8_35jgCqOhyW2B5zjvNE";

    @BeforeEach
    void setup() {
        mockUsuario = createUsuario();
        mockUserDetails = createCustomUserDetails();
        tokenProperties = createAuthenticationProperties();

        when(authenticationProperties.getAccessToken()).thenReturn(tokenProperties.getAccessToken());
        when(authenticationProperties.getRefreshToken()).thenReturn(tokenProperties.getRefreshToken());
    }

    @Test
    void testLoginSuccess() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(USUARIO_USERNAME, USUARIO_PASSWORD);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(usuarioService.findByUsername(USUARIO_USERNAME)).thenReturn(mockUsuario);
        when(authFactory.toCustomUserDetails(mockUsuario)).thenReturn(mockUserDetails);
        when(tokenService.generateAccessToken(mockUserDetails)).thenReturn(accessToken);
        when(tokenService.generateRefreshToken(mockUserDetails)).thenReturn(refreshToken);

        AuthenticationResponse response = service.login(USUARIO_USERNAME, USUARIO_PASSWORD);

        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertEquals(tokenProperties.getAccessToken().getExpirationInSeconds(), response.getExpiresIn());

        verify(tokenService).revogarTokenUsuario(mockUsuario, Arrays.asList(TokenEnum.ACCESS_TOKEN, TokenEnum.REFRESH_TOKEN));
        verify(tokenService).save(mockUsuario, accessToken, TokenEnum.ACCESS_TOKEN);
        verify(tokenService).save(mockUsuario, refreshToken, TokenEnum.REFRESH_TOKEN);
    }

    @Test
    void testLogoutSuccess() {
        when(usuarioService.findByUsername(USUARIO_USERNAME)).thenReturn(mockUsuario);

        service.logout(USUARIO_USERNAME);

        verify(tokenService).revogarTokenUsuario(mockUsuario, Arrays.asList(TokenEnum.ACCESS_TOKEN, TokenEnum.REFRESH_TOKEN));
    }
}
