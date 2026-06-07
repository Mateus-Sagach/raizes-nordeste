package com.raizes.nordeste.api.dto.response;


import com.raizes.nordeste.domain.model.Usuario;
import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        String perfil,
        Boolean ativo,
        Boolean consentimentoLgpd,
        LocalDateTime createdAt
) {
    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil().name(),
                usuario.getAtivo(),
                usuario.getConsentimentoLgpd(),
                usuario.getCreatedAt()
                // senha hash nao via ser colocada pois é um dado que nao deve aparecer na resposta da api
        );
    }
}