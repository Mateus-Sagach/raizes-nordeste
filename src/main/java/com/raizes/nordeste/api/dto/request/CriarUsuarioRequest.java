package com.raizes.nordeste.api.dto.request;


import com.raizes.nordeste.domain.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CriarUsuarioRequest(

        @NotBlank(message = "Nome e obrigatorio")
        @Size(min = 2, max = 150, message = "Nome deve ter entre 2 e 150 caracteres")
        String nome,

        @NotBlank(message = "Email e obrigatorio")
        @Email(message = "Email invalido")
        String email,

        @NotBlank(message = "Senha e obrigatoria")
        @Size(min = 6, message = "Senha deve ter no minimo 6 caracteres")
        String senha,

        @NotNull(message = "Perfil e obrigatorio")
        PerfilUsuario perfil,

        @NotNull(message = "Consentimento LGPD e obrigatorio")
        Boolean consentimentoLgpd
) {}