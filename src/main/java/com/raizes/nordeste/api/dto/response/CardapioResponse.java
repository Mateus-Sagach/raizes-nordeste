package com.raizes.nordeste.api.dto.response;


import com.raizes.nordeste.domain.model.CardapioUnidade;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CardapioResponse {

    private Long produtoId;
    private String nomeProduto;
    private String categoria;
    private BigDecimal precoLocal;
    private boolean disponivel;

    public static CardapioResponse from(CardapioUnidade cardapioUnidade) {
        var produto = cardapioUnidade.getProduto();
        return CardapioResponse.builder()
                .produtoId(produto.getId())
                .nomeProduto(produto.getNome())
                .categoria(produto.getCategoria())
                .precoLocal(cardapioUnidade.getPrecoLocal())
                .disponivel(cardapioUnidade.getDisponivel())
                 .build();
    }
}