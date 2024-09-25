package com.br.contasapagar.domain.service;

import com.br.contasapagar.application.payload.auth.AuthenticationResponse;
import com.br.contasapagar.application.payload.auth.CustomUserDetails;
import com.br.contasapagar.domain.enumeration.TokenEnum;
import com.br.contasapagar.domain.model.Usuario;
import com.br.contasapagar.domain.factory.AuthFactory;
import com.br.contasapagar.infrastructure.properties.AuthenticationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.br.contasapagar.shared.constant.Constants.USERNAME;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthFactory authFactory;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationProperties authenticationProperties;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    @Transactional
    public AuthenticationResponse login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username, password));

        Usuario usuario = usuarioService.findByUsername(authentication.getName());
        CustomUserDetails userDetails = authFactory.toCustomUserDetails(usuario);

        String accessToken = tokenService.generateAccessToken(userDetails);
        String refreshToken = tokenService.generateRefreshToken(userDetails);

        tokenService.revogarTokenUsuario(usuario, Arrays.asList(TokenEnum.ACCESS_TOKEN, TokenEnum.REFRESH_TOKEN));
        tokenService.save(usuario, accessToken, TokenEnum.ACCESS_TOKEN);
        tokenService.save(usuario, refreshToken, TokenEnum.REFRESH_TOKEN);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(authenticationProperties.getAccessToken().getExpirationInSeconds())
                .build();
    }

    @Transactional
    public AuthenticationResponse refreshToken(String refreshToken) {
        Jws<Claims> claimsJws = tokenService.validateToken(refreshToken);

        Claims payload = claimsJws.getPayload();
        Usuario usuario = usuarioService.findByUsername((String) payload.get(USERNAME));
        CustomUserDetails userDetails = authFactory.toCustomUserDetails(usuario);

        String accessToken = tokenService.generateAccessToken(userDetails);

        tokenService.revogarTokenUsuario(usuario, List.of(TokenEnum.ACCESS_TOKEN));
        tokenService.save(usuario, accessToken, TokenEnum.ACCESS_TOKEN);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(authenticationProperties.getAccessToken().getExpirationInSeconds())
                .build();
    }

    @Transactional
    public void logout(String username) {
        Usuario usuario = usuarioService.findByUsername(username);

        tokenService.revogarTokenUsuario(usuario, Arrays.asList(TokenEnum.ACCESS_TOKEN, TokenEnum.REFRESH_TOKEN));
    }
}
