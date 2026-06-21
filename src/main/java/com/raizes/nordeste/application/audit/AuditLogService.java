package com.raizes.nordeste.application.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raizes.nordeste.domain.model.AuditLog;
import com.raizes.nordeste.domain.model.Usuario;
import com.raizes.nordeste.infrastructure.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

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

    // Outra opção que aceita dados extras (por exemplo o motivo de cancelamento) e os persiste como JSON na coluna dados_json, ja existente na tabela audit_log
    @Async
    public void registrar(Usuario usuario,
                          String acao,
                          String entidade,
                          Long entidadeId,
                          Map<String, Object> dados) {

        AuditLog log = new AuditLog();
        log.setUsuario(usuario);
        log.setAcao(acao);
        log.setEntidade(entidade);
        log.setEntidadeId(entidadeId);
        log.setDadosJson(serializar(dados));
        auditLogRepository.save(log);
    }

    private String serializar(Map<String, Object> dados) {
        if (dados == null || dados.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(dados);
        } catch (JsonProcessingException e) {
            //uma falha de serializacao nao deve quebrar o registro de auditoria nem o fluxo principal (como por exemplo o cancelamento do pedido)
            return "{\"erro\":\"falha ao tentar  serializar dados do log\"}";
        }
    }
}