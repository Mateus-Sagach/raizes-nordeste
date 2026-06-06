package com.raizes.nordeste.domain.enums;

public enum StatusPedido {
    AGUARDANDO_PAGAMENTO,
    EM_PREPARACAO,
    PRONTO,
    ENTREGUE,
    CANCELADO;

    public boolean podeTransicionarPara(StatusPedido destino) {
        return switch (this) {
            case AGUARDANDO_PAGAMENTO -> destino == EM_PREPARACAO
                                      || destino == CANCELADO;
            case EM_PREPARACAO -> destino == PRONTO
                               || destino == CANCELADO;
            case PRONTO -> destino == ENTREGUE;
            case ENTREGUE, CANCELADO -> false;
        };
    }
}
