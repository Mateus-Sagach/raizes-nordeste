package com.raizes.nordeste.api.dto.response;

import com.raizes.nordeste.domain.model.Estoque;

public record EstoqueResponse(
        Long id,
        Long unidadeId,
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        Integer quantidadeMinima,
        Boolean alertaReposicao
) {
    public static EstoqueResponse from(Estoque estoque) {
        return new EstoqueResponse(
                estoque.getId(),
                estoque.getUnidade().getId(),
                estoque.getProduto().getId(),
                estoque.getProduto().getNome(),
                estoque.getQuantidade(),
                estoque.getQuantidadeMinima(),
                estoque.estaAbaixoMinimo()
        );
    }
}