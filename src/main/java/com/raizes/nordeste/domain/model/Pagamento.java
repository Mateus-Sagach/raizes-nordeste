package com.raizes.nordeste.domain.model;


import com.raizes.nordeste.domain.enums.FormaPagamento;
import com.raizes.nordeste.domain.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
@Getter
@Setter
@NoArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false, length = 20)
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPagamento status = StatusPagamento.PENDENTE;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "gateway_ref", length = 100)
    private String gatewayRef;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    //Regra de negócio que registra a aprovação do gateway
    public void registrarAprovacao(String ref) {
        this.status = StatusPagamento.APROVADO;
        this.gatewayRef = ref;
    }

    // Regra de negócio que registra a recusa do gateway
    public void registrarRecusa() {
        this.status = StatusPagamento.RECUSADO;
    }

    public boolean isPago() {
        return this.status == StatusPagamento.APROVADO;
    }
}