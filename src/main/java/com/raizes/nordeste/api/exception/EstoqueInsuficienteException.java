package com.raizes.nordeste.api.exception;


public class EstoqueInsuficienteException extends RuntimeException {

    private final int disponivel;

    public EstoqueInsuficienteException(Long produtoId, int disponivel) {
        super("Estoque insuficiente para o produto " + produtoId
                + ". Disponível: " + disponivel);
        this.disponivel = disponivel;
    }

    public int getDisponivel() {
        return disponivel;
    }
}