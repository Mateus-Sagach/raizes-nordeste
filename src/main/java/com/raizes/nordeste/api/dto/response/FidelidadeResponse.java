package com.raizes.nordeste.api.dto.response;


import com.raizes.nordeste.domain.model.Fidelidade;

public record FidelidadeResponse(
        Long id,
        Long clienteId,
        String nomeCliente,
        Integer saldoPontos,
        Integer totalAcumulado,
        Boolean consentimentoAtivo
) {
    public static FidelidadeResponse from(Fidelidade fidelidade) {
        return new FidelidadeResponse(
                fidelidade.getId(),
                fidelidade.getCliente().getId(),
                fidelidade.getCliente().getNome(),
                fidelidade.getSaldoPontos(),
                fidelidade.getTotalAcumulado(),
                fidelidade.getCliente().getConsentimentoLgpd()
        );
    }
}