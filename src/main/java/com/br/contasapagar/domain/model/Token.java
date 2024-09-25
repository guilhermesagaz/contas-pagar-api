package com.br.contasapagar.domain.model;

import com.br.contasapagar.domain.enumeration.TokenEnum;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenEnum tipo;

    private boolean revogado;

    @ManyToOne
    private Usuario usuario;

    public void revogar() {
        this.setRevogado(true);
    }
}
