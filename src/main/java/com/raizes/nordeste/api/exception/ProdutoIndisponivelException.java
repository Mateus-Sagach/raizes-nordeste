package com.raizes.nordeste.api.exception;

public class ProdutoIndisponivelException extends RuntimeException {

    public ProdutoIndisponivelException(Long produtoId) {
        super("Produto " + produtoId + " nao esta disponivel nesta unidade.");
    }
}