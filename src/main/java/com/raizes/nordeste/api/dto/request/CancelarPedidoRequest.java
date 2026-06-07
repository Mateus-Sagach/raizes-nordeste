package com.raizes.nordeste.api.dto.request;


import jakarta.validation.constraints.NotBlank;

public record CancelarPedidoRequest(

        @NotBlank(message = "Motivo do cancelamento e obrigatorio")
        String motivo
) {}