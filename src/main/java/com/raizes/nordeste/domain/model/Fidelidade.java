package com.raizes.nordeste.domain.model;


import com.raizes.nordeste.api.exception.SaldoInsuficienteException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "fidelidade")
@Getter
@Setter
@NoArgsConstructor
public class Fidelidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Usuario cliente;

    @Column(name = "saldo_pontos", nullable = false)
    private Integer saldoPontos = 0;

    @Column(name = "total_acumulado", nullable = false)
    private Integer totalAcumulado = 0;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Regraa de negócio que acumula os pontos após compra
    public void acumularPontos(int pontos) {
        if (pontos <= 0) return;
        this.saldoPontos  += pontos;
        this.totalAcumulado += pontos;
        this.updatedAt = LocalDateTime.now();
    }

    //Regra de negócio que resgata os pontos para desconto
    public void resgatarPontos(int pontos) {
        if (pontos > this.saldoPontos) {
            throw new SaldoInsuficienteException(
                    this.saldoPontos, pontos);
        }
        this.saldoPontos -= pontos;
        this.updatedAt = LocalDateTime.now();
    }
}