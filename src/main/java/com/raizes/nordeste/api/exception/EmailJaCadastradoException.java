package com.raizes.nordeste.api.exception;


public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException(String email) {
        super("Email " + email + " ja esta cadastrado.");
    }
}