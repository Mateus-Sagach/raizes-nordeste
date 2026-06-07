package com.raizes.nordeste.api.exception;


import com.raizes.nordeste.domain.enums.StatusPedido;

public class TransicaoStatusInvalidaException extends RuntimeException {

    public TransicaoStatusInvalidaException(StatusPedido atual,
                                            StatusPedido destino) {
        super("Não é possível transicionar o status de "
                + atual.name() + " para " + destino.name());
    }
}