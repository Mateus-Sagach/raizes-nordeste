package com.raizes.nordeste.infrastructure.gateway;


import com.raizes.nordeste.domain.enums.StatusPagamento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@Slf4j
public class PagamentoGatewayMock {

    private static final double TAXA_APROVACAO = 0.0; //taxa para aprovação para testar pedido de id impar, esta em 0 para que todos os pedidos de id impar sejam recusados

    public GatewayResponse processar(Long pedidoId,
                                     BigDecimal valor,
                                     String formaPagamento) {

        log.info("Gateway mock processando pedido {} valor {}",
                pedidoId, valor);

        boolean aprovado = pedidoId % 2 == 0
                || Math.random() < TAXA_APROVACAO;

        if (aprovado) {
            String ref = "mock-ref-" +
                    UUID.randomUUID().toString().substring(0, 8);
            log.info("Pagamento APROVADO ref {}", ref);
            return new GatewayResponse(
                    StatusPagamento.APROVADO, ref,
                    "Pagamento aprovado com sucesso");
        }

        log.warn("Pagamento RECUSADO pedido {}", pedidoId);
        return new GatewayResponse(
                StatusPagamento.RECUSADO, null,
                "Pagamento recusado pela operadora");
    }

    public record GatewayResponse(
            StatusPagamento status,
            String gatewayRef,
            String mensagem
    ) {}
}