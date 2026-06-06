package com.raizes.nordeste.infrastructure.repository;


import com.raizes.nordeste.domain.enums.CanalPedido;
import com.raizes.nordeste.domain.enums.StatusPedido;
import com.raizes.nordeste.domain.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    //Função que busca pedidos de um cliente especifico
    List<Pedido> findByClienteId(Long clienteId);

    //busca pedidos filtrando por canal
    //usado no endpoint GET /pedidos?canalPedido=TOTEM
    Page<Pedido> findByCanalPedido(CanalPedido canalPedido, Pageable pageable);

    // Busca pedidos filtrando por status
    Page<Pedido> findByStatus(StatusPedido status, Pageable pageable);

    //Busca pedidos de uma unidade especifica
    Page<Pedido> findByUnidadeId(Long unidadeId, Pageable pageable);

    // Busca pedidos filtrando por canal e o status ao mesmo tempo
    //Usado quando o gerente filtra por canal e status simultaneamente
    Page<Pedido> findByCanalPedidoAndStatus(
            CanalPedido canalPedido,
            StatusPedido status,
            Pageable pageable);
}