package com.raizes.nordeste.api.dto.response;


import com.raizes.nordeste.domain.model.Pedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long pedidoId,
        String canalPedido,
        String status,
        BigDecimal total,
        List<ItemPedidoResponse> itens,
        PagamentoResponse pagamento,
        LocalDateTime criadoEm
) {
    public static PedidoResponse from(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getCanalPedido().name(),
                pedido.getStatus().name(),
                pedido.getTotal(),
                pedido.getItens().stream()
                        .map(ItemPedidoResponse::from)
                         .toList(),
                pedido.getPagamento() != null
                        ? PagamentoResponse.from(pedido.getPagamento())
                        : null,
                pedido.getCreatedAt()
        );
    }
}