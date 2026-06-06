package com.raizes.nordeste.infrastructure.repository;


import com.raizes.nordeste.domain.model.TransacaoPontos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoPontosRepository extends JpaRepository<TransacaoPontos, Long> {

    // Busca historico de transacoes de um programa de fidelidade, retorna paginado porque pode ter muitas transacoes
    Page<TransacaoPontos> findByFidelidadeId(
            Long fidelidadeId, Pageable pageable);
}