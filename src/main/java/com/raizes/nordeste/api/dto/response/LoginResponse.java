package com.raizes.nordeste.api.dto.response;

public record LoginResponse(
        String accessToken,
        String tokenType,
        Long expiresIn,
        UsuarioInfo user
) {
    //essa classe sera usada só aqui por isso nao foi criado um response separado
    public record UsuarioInfo(
            Long id,
            String nome,
            String perfil
    ) {}
}