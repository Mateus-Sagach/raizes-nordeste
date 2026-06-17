package com.raizes.nordeste.api.exception;


public class EstoqueNaoEncontradoException extends RuntimeException {

    public EstoqueNaoEncontradoException(Long unidadeId, Long produtoId) {
        super("Estoque nao foi  encontrado para o produto " + produtoId + " na unidade " + unidadeId + ".");
    }
}