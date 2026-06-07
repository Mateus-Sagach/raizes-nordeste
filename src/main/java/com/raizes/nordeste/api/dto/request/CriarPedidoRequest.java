package com.raizes.nordeste.api.dto.request;


import com.raizes.nordeste.domain.enums.CanalPedido;
import com.raizes.nordeste.domain.enums.FormaPagamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CriarPedidoRequest(

        @NotNull(message = "Unidade e obrigatoria")
        Long unidadeId,

        @NotNull(message = "Canal do pedido e obrigatorio")
        CanalPedido canalPedido,

        @NotNull(message = "Itens sao obrigatorios")
        @NotEmpty(message = "Pedido deve ter ao menos um item")    //garante que a lista tem pelo menos um item
        @Valid
        List<ItemPedidoRequest> itens,

        @NotNull(message = "Forma de pagamento e obrigatoria")
        FormaPagamento formaPagamento
) {}