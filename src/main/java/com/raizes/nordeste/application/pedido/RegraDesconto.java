package com.raizes.nordeste.application.pedido;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/*
  Ponto de extensao para regras de desconto/promocao em um futuro quando necessário.

  A implementacao atual SemDesconto nao aplica nenhum desconto. Regras futuras  podem ser adicionadas implementando esta interface, sem precisar alterar o PedidoService
  nem o fluxo de criacao de pedido .
 */

public interface RegraDesconto {

    BigDecimal calcularDesconto(BigDecimal totalBruto, int saldoPontosFidelidade);

    @Component
    class SemDesconto implements RegraDesconto {
        @Override
        public BigDecimal calcularDesconto(BigDecimal totalBruto, int saldoPontosFidelidade) {
            return BigDecimal.ZERO;
        }
    }
}