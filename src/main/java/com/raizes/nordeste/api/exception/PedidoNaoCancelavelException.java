package com.raizes.nordeste.api.exception;


public class PedidoNaoCancelavelException extends RuntimeException {

    public PedidoNaoCancelavelException(Long pedidoId) {
        super("Pedido " + pedidoId
                + " não pode ser cancelado pois já foi entregue.");
    }
}