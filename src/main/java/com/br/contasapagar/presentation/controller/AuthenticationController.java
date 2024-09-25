package com.br.contasapagar.presentation.controller;

import com.br.contasapagar.application.payload.auth.AuthenticationRequest;
import com.br.contasapagar.application.payload.auth.AuthenticationResponse;
import com.br.contasapagar.application.payload.auth.RefreshTokenRequest;
import com.br.contasapagar.domain.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok().body(authenticationService.login(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody @Valid RefreshTokenRequest request){
        return ResponseEntity.ok().body(authenticationService.refreshToken(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout(Principal principal){
        authenticationService.logout(principal.getName());
        return ResponseEntity.noContent().build();
    }
}
