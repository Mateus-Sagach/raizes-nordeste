package com.raizes.nordeste.domain.model;


import com.raizes.nordeste.api.exception.PedidoNaoCancelavelException;
import com.raizes.nordeste.api.exception.TransicaoStatusInvalidaException;
import com.raizes.nordeste.domain.enums.CanalPedido;
import com.raizes.nordeste.domain.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.raizes.nordeste.domain.model.Pagamento;


@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal_pedido", nullable = false, length = 20)
    private CanalPedido canalPedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusPedido status = StatusPedido.AGUARDANDO_PAGAMENTO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(mappedBy = "pedido",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    @OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
    private Pagamento pagamento;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Regra de negócio que valida e aplica transição de status
    public void atualizarStatus(StatusPedido novoStatus) {
        if (!this.status.podeTransicionarPara(novoStatus)) {
            throw new TransicaoStatusInvalidaException(
                    this.status, novoStatus);
        }
        this.status = novoStatus;
        this.updatedAt = LocalDateTime.now();
    }

    // Regra de negócio que cancela o pedido
    public void cancelar() {
        if (this.status == StatusPedido.ENTREGUE) {
            throw new PedidoNaoCancelavelException(this.id);
        }
        this.status = StatusPedido.CANCELADO;
        this.updatedAt = LocalDateTime.now();
    }

    // Regra de negócio que calcula o total somando os itens
    public BigDecimal calcularTotal() {
        this.total = itens.stream()
                .map(i -> i.getPrecoUnitario()
                        .multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return this.total;
    }

    // Verifica se o pedido ainda pode ser pago
    public boolean isPagavel() {
        return this.status == StatusPedido.AGUARDANDO_PAGAMENTO;
    }
}