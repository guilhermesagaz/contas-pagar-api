package com.br.contasapagar.application.payload.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {

    @NotBlank(message = "O campo refresh token é obrigatório e deve ser informado")
    private String refreshToken;
}
