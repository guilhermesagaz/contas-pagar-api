package com.br.contasapagar.application.payload.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponse {

    private String accessToken;
    private String refreshToken;
    private Integer expiresIn;
}
