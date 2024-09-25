package com.br.contasapagar.domain.repository;

import com.br.contasapagar.domain.enumeration.TokenEnum;
import com.br.contasapagar.domain.model.Token;
import com.br.contasapagar.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    List<Token> findByUsuarioAndRevogado(Usuario usuario, boolean revogado);

    Optional<Token> findByTokenAndRevogado(String token, boolean revogado);

    Optional<Token> findByUsuarioAndTipoAndRevogado(Usuario usuario, TokenEnum tipo, boolean revogado);

    @Query("""
            SELECT t FROM Token t
             WHERE t.usuario = :usuario
               AND t.tipo IN(:tipoList)
               AND t.revogado = false
            """)
    List<Token> findByUsuarioAndTipoIn(Usuario usuario, List<TokenEnum> tipoList);
}
