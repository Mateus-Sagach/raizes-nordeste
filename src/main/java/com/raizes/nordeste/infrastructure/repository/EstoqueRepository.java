package com.raizes.nordeste.infrastructure.repository;


import com.raizes.nordeste.domain.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    //Vai buscar o estoque de um produto especifico em em uma unidade
    Optional<Estoque> findByUnidadeIdAndProdutoId(
            Long unidadeId, Long produtoId);

    // Vai busca tudo no estoque de uma unidade
    List<Estoque> findByUnidadeId(Long unidadeId);

    // Busca itens abaixo do minimo usando JPQL
    //JPQL e um linguagem de consulta do JPA que usa nomes das classe  Java em vez de nomes das tabelas SQL
    @Query("SELECT e FROM Estoque e " +
             "WHERE e.unidade.id = :unidadeId " +
            "AND e.quantidade < e.quantidadeMinima")
    List<Estoque> findItensAbaixoDoMinimo(
            @Param("unidadeId") Long unidadeId);
}