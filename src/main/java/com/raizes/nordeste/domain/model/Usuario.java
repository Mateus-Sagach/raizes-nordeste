package com.raizes.nordeste.domain.model;

import com.raizes.nordeste.domain.enums.PerfilUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor

public class Usuario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 150)
        private String nome;

        @Column(nullable = false, length = 150, unique = true)
        private String email;

        @Column(name = "senha_hash", nullable = false)
        private String senhaHash;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 30)
        private PerfilUsuario perfil;

        @Column(nullable = false)
        private Boolean ativo = true;

        @Column(name = "consentimento_lgpd", nullable = false)
        private Boolean consentimentoLgpd = false;

        @Column(name = "data_consentimento")
        private LocalDateTime dataConsentimento;

        @CreationTimestamp
        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        //Regra de negocio para atender LGPD para anonimizar dados do usuário
        public void anonimizarDados() {
            this.nome  = "Usuário Removido";
            this.email = "removido_" + this.id + "@anonimizado.com";
            this.ativo = false;
            this.consentimentoLgpd = false;
        }

        //Verifica se o usuário tem um determinado perfil
        public boolean temPerfil(PerfilUsuario perfilVerificado) {
            return this.perfil == perfilVerificado;
        }
    }

