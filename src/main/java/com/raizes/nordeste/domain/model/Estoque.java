package com.raizes.nordeste.domain.model;

import com.raizes.nordeste.api.exception.EstoqueInsuficienteException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "estoque",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"unidade_id", "produto_id"}))
@Getter
@Setter
@NoArgsConstructor
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade = 0;

    @Column(name = "quantidade_minima", nullable = false)
    private Integer quantidadeMinima = 10;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Regra de negócio que verifica disponibilidade
    public boolean temDisponibilidade(int qtd) {
        return this.quantidade >= qtd;
    }

    //Regra de negócio que decrementa após pagamento aprovado
    public void decrementar(int qtd) {
        if (!temDisponibilidade(qtd)) {
            throw new EstoqueInsuficienteException(
                    this.produto.getId(), this.quantidade);
        }
        this.quantidade -= qtd;
        this.updatedAt = LocalDateTime.now();
    }

    //Regra de negócio que incrementa na entrada de mercadoria
    public void incrementar(int qtd) {
        this.quantidade += qtd;
        this.updatedAt = LocalDateTime.now();
    }

    // Verifica se precisa de reposição
    public boolean estaAbaixoMinimo() {
        return this.quantidade < this.quantidadeMinima;
    }
}