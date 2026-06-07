package com.raizes.nordeste.api.exception;

public class SaldoInsuficienteException extends RuntimeException {

    public SaldoInsuficienteException(int saldoAtual, int pontosRequeridos) {
        super("Saldo insuficiente. Saldo atual: " + saldoAtual
                + ". Pontos requeridos: " + pontosRequeridos);
    }
}