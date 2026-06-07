package com.raizes.nordeste.api.dto.response;


import java.time.LocalDateTime;

public record AtualizarStatusResponse(
        Long pedidoId,
        String statusAnterior,
        String statusAtual,
        LocalDateTime atualizadoEm
) {}