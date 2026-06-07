package com.raizes.nordeste.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemPedidoRequest(

        @NotNull(message = "Produto e obrigatorio")
        Long produtoId,

        @NotNull(message = "Quantidade e obrigatoria")
        @Min(value = 1, message = "Quantidade minima e 1")
        Integer quantidade
) {}