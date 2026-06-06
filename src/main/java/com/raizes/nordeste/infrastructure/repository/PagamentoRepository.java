package com.raizes.nordeste.infrastructure.repository;


import com.raizes.nordeste.domain.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    //Procura o pagamento de um pedido especifico
    Optional<Pagamento> findByPedidoId(Long pedidoId);

    // Verifica se ja existe pagamento para um pedido
    // Usado para a logica do PagamentoService
    boolean existsByPedidoId(Long pedidoId);
}