package com.br.contasapagar.domain.repository;

import com.br.contasapagar.domain.model.EnvioArquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvioArquivoRepository extends JpaRepository<EnvioArquivo, Long> {
}
