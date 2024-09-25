package com.br.contasapagar.application.filter;

import com.br.contasapagar.application.exception.TokenInvalidException;
import com.br.contasapagar.domain.service.TokenService;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenVerifierFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";
    private static final String USERNAME = "username";
    private static final String AUTHORITIES = "authorities";

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith(BEARER)){
            this.doFilter(request, response, filterChain);
            return;
        }

        String token = authorizationHeader.replace(BEARER, "");

        try{
            Jws<Claims> claimsJws = tokenService.validateToken(token);
            Claims payload = claimsJws.getPayload();

            String username = (String) payload.get(USERNAME);

            if(isNull(payload.get(AUTHORITIES))) {
                throw new MissingClaimException(claimsJws.getHeader(), payload, USERNAME, username, "Token is invalid");
            }

            Set<SimpleGrantedAuthority> authorities = Arrays.stream(((String) payload.get(AUTHORITIES)).split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new TokenInvalidException("Token do usuário é invalido.");
        }

        this.doFilter(request, response, filterChain);
    }
}
