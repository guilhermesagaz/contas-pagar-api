package com.br.contasapagar.domain.model;

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
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String username;

    private String password;

    private boolean expirado;

    private boolean bloqueado;

    private boolean credenciaisExpiradas;

    private boolean ativo;

    @Builder.Default
    @ManyToMany(cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_perfil",
            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id", referencedColumnName = "id"))
    private List<Perfil> perfis = new ArrayList<>();
}
