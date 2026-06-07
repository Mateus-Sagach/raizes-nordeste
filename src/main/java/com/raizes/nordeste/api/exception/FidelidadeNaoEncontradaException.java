package com.raizes.nordeste.api.exception;


public class FidelidadeNaoEncontradaException extends RuntimeException {

    public FidelidadeNaoEncontradaException(Long clienteId) {
        super("Programa de fidelidade nao encontrado " +
                "para o cliente " + clienteId + ".");
    }
}