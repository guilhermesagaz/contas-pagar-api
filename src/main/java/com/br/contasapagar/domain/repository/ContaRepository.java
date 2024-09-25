package com.br.contasapagar.domain.repository;

import com.br.contasapagar.domain.model.Conta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    @Query("""
            SELECT conta
              FROM Conta conta
             WHERE conta.situacao = 'PENDENTE'
               AND conta.dataVencimento < : data
            """)
    List<Conta> findAllAtrasadosNaData(LocalDate data);

    @Query("""
            SELECT conta
              FROM Conta conta
             WHERE (:descricao IS NULL OR conta.descricao LIKE %:descricao%)
               AND (:dataVencimento IS NULL OR conta.dataVencimento = :dataVencimento)
            """)
    Page<Conta> findAllPaged(String descricao, LocalDate dataVencimento, Pageable pageable);

    @Query("""
            SELECT sum(conta.valor)
              FROM Conta conta
             WHERE conta.situacao = 'PAGO'
               AND conta.dataPagamento BETWEEN :dataInicial AND :dataFinal
            """)
    BigDecimal sumValorByDataPagamentoBetween(LocalDate dataInicial, LocalDate dataFinal);
}
