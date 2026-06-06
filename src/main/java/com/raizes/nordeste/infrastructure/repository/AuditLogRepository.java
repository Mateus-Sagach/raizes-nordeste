package com.raizes.nordeste.infrastructure.repository;


import com.raizes.nordeste.domain.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    //Procura todos os logs de um usuario especifico
    List<AuditLog> findByUsuarioId(Long usuarioId);

    //Busca logs por tipo de acao com paginacao, Exemplo: buscar todos os CANCELAMENTOS
    Page<AuditLog> findByAcao(String acao, Pageable pageable);

    // Busca logs de uma entidade especifica, Exemplo: todos os logs do Pedido de id 1111
    List<AuditLog> findByEntidadeAndEntidadeId(
            String entidade, Long entidadeId);
}