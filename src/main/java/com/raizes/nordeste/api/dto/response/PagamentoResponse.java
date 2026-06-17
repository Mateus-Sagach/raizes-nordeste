package com.raizes.nordeste.api.dto.response;


import com.raizes.nordeste.domain.model.Pagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoResponse(
        Long id,
        String formaPagamento,
        String status,
        BigDecimal valor,
        String gatewayRef,
        LocalDateTime createdAt
) {
    public static PagamentoResponse from(Pagamento pagamento) {
        return new PagamentoResponse(
                pagamento.getId(),
                pagamento.getFormaPagamento().name(),
                pagamento.getStatus().name(),
                pagamento.getValor(),
                 pagamento.getGatewayRef(),
                pagamento.getCreatedAt()
        );
    }
}