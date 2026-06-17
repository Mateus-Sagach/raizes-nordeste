package com.raizes.nordeste.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "cardapio_unidade",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"unidade_id", "produto_id"}))
@Getter
@Setter
@NoArgsConstructor
public class CardapioUnidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "preco_local", nullable = false,
            precision = 10, scale = 2)
    private BigDecimal precoLocal;

    @Column(nullable = false)
    private Boolean disponivel = true;

    //produto só pode ser vendido se disponível
    public boolean isDisponivelParaVenda() {
        return this.disponivel && this.produto.getDisponivel();
    }
}