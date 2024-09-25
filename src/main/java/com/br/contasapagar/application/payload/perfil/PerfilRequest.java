package com.br.contasapagar.application.payload.perfil;

import com.br.contasapagar.domain.enumeration.TipoPerfilEnum;
import jakarta.validation.constraints.Max;
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
public class PerfilRequest {

    @NotNull(message = "O campo tipo é obrigatório e deve ser informado")
    private TipoPerfilEnum tipo;

    @NotNull(message = "O campo nome é obrigatório e deve ser informado")
    @Size(max = 100, message = "O tamanho do campo nome deve ser menor ou igual à {max}")
    private String nome;

    @NotBlank(message = "O campo descrição é obrigatório e deve ser informado")
    @Size(max = 400, message = "O tamanho do campo descrição deve ser menor ou igual à {max}")
    private String descricao;

    @Builder.Default
    private List<Long> permissoesIds = new ArrayList<>();
}
