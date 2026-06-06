package com.raizes.nordeste.infrastructure.repository;


import com.raizes.nordeste.domain.model.CardapioUnidade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CardapioUnidadeRepository
        extends JpaRepository<CardapioUnidade, Long> {

    // Buscara todos os itens do cardapio de uma unidade especifica
    //usar no endpoint GET /unidades/{id}/cardapio
    List<CardapioUnidade> findByUnidadeIdAndDisponivelTrue(Long unidadeId);

    // Busca um produto especifico no cardapio de uma unidade
    //Usado na criacao do pedido para pegar o preco local
    Optional<CardapioUnidade> findByUnidadeIdAndProdutoIdAndDisponivelTrue(
            Long unidadeId, Long produtoId);
}