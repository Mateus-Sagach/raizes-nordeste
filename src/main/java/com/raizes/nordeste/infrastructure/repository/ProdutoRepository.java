package com.raizes.nordeste.infrastructure.repository;


import com.raizes.nordeste.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    //Vai buscar todos os produtos disponiveis
    List<Produto> findByDisponivelTrue();

    // Busca produtos por categoria
    List<Produto> findByCategoriaAndDisponivelTrue(String categoria);
}