package com.br.contasapagar.domain.model;

import com.br.contasapagar.domain.enumeration.TipoPerfilEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoPerfilEnum tipo;

    private String nome;

    private String descricao;

    @Builder.Default
    @ManyToMany(cascade= CascadeType.ALL)
    @JoinTable(name = "permissao_perfil",
            joinColumns = @JoinColumn(name = "perfil_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permissao_id", referencedColumnName = "id"))
    private List<Permissao> permissoes = new ArrayList<>();
}
