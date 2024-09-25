package com.br.contasapagar.application.payload.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "O campo username é obrigatório e deve ser informado")
    private String username;

    @NotBlank(message = "O campo password é obrigatório e deve ser informado")
    private String password;
}
