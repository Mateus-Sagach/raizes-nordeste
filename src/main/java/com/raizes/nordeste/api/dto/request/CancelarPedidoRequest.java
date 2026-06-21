package com.raizes.nordeste.api.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CancelarPedidoRequest(

        @NotBlank(message = "Motivo do  cancelamento e obrigatorio")
        @Size(max = 500, message = "Motivo deve ter no maximo 500 caracteres")
        String motivo
) {}