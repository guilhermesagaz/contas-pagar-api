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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.br.contasapagar.shared.constant.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository repository;
    private final AuthenticationProperties authenticationProperties;
    private final MessageHandler messageHandler;

    public Token save(Usuario usuario, String token, TokenEnum tipo) {
        return repository.save(Token.builder()
                .token(token)
                .usuario(usuario)
                .tipo(tipo)
                .revogado(false)
                .build());
    }

    public void revogarTokenUsuario(Usuario usuario, List<TokenEnum> tiposTokens) {
        List<Token> tokens = repository.findByUsuarioAndTipoIn(usuario, tiposTokens);
        tokens.forEach(Token::revogar);

        repository.saveAll(tokens);
    }

    public String generateAccessToken(CustomUserDetails userDetails) {
        List<SimpleGrantedAuthority> authoritiesList = (List<SimpleGrantedAuthority>) userDetails.getAuthorities();

        Map<String, Object> tokenBody = new HashMap<>();
        tokenBody.put(ID, userDetails.getId());
        tokenBody.put(NOME, userDetails.getNome());
        tokenBody.put(USERNAME, userDetails.getUsername());
        tokenBody.put(VERIFICADO, userDetails.isVerified());
        tokenBody.put(ATIVO, userDetails.isEnabled());
        tokenBody.put(AUTHORITIES, authoritiesList.stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")));

        return createJwt(tokenBody,
                authenticationProperties.getAccessToken().getExpirationInSeconds(),
                authenticationProperties.getSecretKey().getBytes());
    }

    public String generateRefreshToken(CustomUserDetails userDetails) {
        Map<String, Object> tokenBody = new HashMap<>();
        tokenBody.put(ID, userDetails.getId());
        tokenBody.put(USERNAME, userDetails.getUsername());

        return createJwt(tokenBody,
                authenticationProperties.getRefreshToken().getExpirationInSeconds(),
                authenticationProperties.getSecretKey().getBytes());
    }

    public Jws<Claims> validateToken(String token) {
        repository.findByTokenAndRevogado(token, false)
                .orElseThrow(() -> new TokenInvalidException(messageHandler.getMessage(TOKEN_INVALID)));

        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(authenticationProperties.getSecretKey().getBytes()))
                .build()
                .parseSignedClaims(token);
    }

    private String createJwt(Map<String, Object> body, Integer expiration, byte[] signingKey) {
        try {
            return Jwts.builder()
                    .claims(body)
                    .issuedAt(new Date())
                    .issuer(CONTAS_PAGAR)
                    .subject((String) body.get(USERNAME))
                    .expiration(genExpirationDate(expiration))
                    .signWith(Keys.hmacShaKeyFor(signingKey))
                    .compact();
        } catch (Exception e){
            throw new TokenInvalidException(messageHandler.getMessage(TOKEN_INVALID));
        }
    }

    private Date genExpirationDate(Integer expiration) {
        return Date.from(LocalDateTime.now().atZone(ZoneId.of("America/Sao_Paulo")).plusSeconds(expiration).toInstant());
    }
}
