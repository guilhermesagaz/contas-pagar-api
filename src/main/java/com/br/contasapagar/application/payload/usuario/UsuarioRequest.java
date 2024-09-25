package com.br.contasapagar.application.payload.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequest {

    @NotBlank(message = "O campo nome é obrigatório e deve ser informado")
    @Size(max = 100, message = "O tamanho do campo nome deve ser menor ou igual à {max}")
    private String nome;

    @NotBlank(message = "O campo email é obrigatório e deve ser informado")
    @Size(max = 255, message = "O tamanho do campo email deve ser menor ou igual à {max}")
    private String email;

    @NotBlank(message = "O campo password é obrigatório e deve ser informado")
    @Size(max = 255, message = "O tamanho do campo password deve ser menor ou igual à {max}")
    private String password;

    @Builder.Default
    private List<Long> perfisIds = new ArrayList<>();
}
