package com.raizes.nordeste.api.dto.request;


import com.raizes.nordeste.domain.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusRequest(

        @NotNull(message = "Status e obrigatorio")
        StatusPedido status
) {}