package com.br.contasapagar.domain.repository;

import com.br.contasapagar.domain.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    boolean existsByNome(String nome);
}
