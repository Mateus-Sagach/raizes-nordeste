package com.raizes.nordeste.api.exception;


public class FidelidadeNaoEncontradaException extends RuntimeException {

    public FidelidadeNaoEncontradaException(Long clienteId) {
        super("Programa de fidelidade nao foi  encontrado " +
                "para o cliente " + clienteId + ".");
    }
}