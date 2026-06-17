package com.raizes.nordeste.application.audit;


import com.raizes.nordeste.domain.model.AuditLog;
import com.raizes.nordeste.domain.model.Usuario;
import com.raizes.nordeste.infrastructure.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Async
    public void registrar(Usuario usuario,
                          String acao,
                           String entidade,
                          Long entidadeId) {

        AuditLog log = new AuditLog();
        log.setUsuario(usuario);
        log.setAcao(acao);
        log.setEntidade(entidade);
        log.setEntidadeId(entidadeId);
        auditLogRepository.save(log);
    }
}