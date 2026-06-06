package com.raizes.nordeste.application.estoque;


import com.raizes.nordeste.api.dto.request.EntradaEstoqueRequest;
import com.raizes.nordeste.api.dto.response.EstoqueResponse;
import com.raizes.nordeste.api.exception.EstoqueInsuficienteException;
import com.raizes.nordeste.api.exception.EstoqueNaoEncontradoException;
import com.raizes.nordeste.application.audit.AuditLogService;
import com.raizes.nordeste.domain.model.Estoque;
import com.raizes.nordeste.domain.model.Usuario;
import com.raizes.nordeste.infrastructure.repository.EstoqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final AuditLogService auditLogService;

    @Transactional(readOnly = true)
    public void validarDisponibilidade(Long unidadeId,
                                       Long produtoId,
                                       int quantidade) {
        Estoque estoque = buscarOuErro(unidadeId, produtoId);

        if (!estoque.temDisponibilidade(quantidade)) {
            throw new EstoqueInsuficienteException(
                    produtoId, estoque.getQuantidade());
        }
    }

    @Transactional
    public void decrementar(Long unidadeId,
                            Long produtoId,
                            int quantidade) {
        Estoque estoque = buscarOuErro(unidadeId, produtoId);
        estoque.decrementar(quantidade);
        estoqueRepository.save(estoque);
    }

    @Transactional
    public EstoqueResponse registrarEntrada(
            EntradaEstoqueRequest req,
            Usuario responsavel) {

        Estoque estoque = buscarOuErro(
                req.unidadeId(), req.produtoId());

        int anterior = estoque.getQuantidade();
        estoque.incrementar(req.quantidade());
        estoqueRepository.save(estoque);

        auditLogService.registrar(
                responsavel,
                "ENTRADA_ESTOQUE",
                "Estoque",
                estoque.getId());

        return EstoqueResponse.from(estoque);
    }

    @Transactional(readOnly = true)
    public List<EstoqueResponse> buscarPorUnidade(Long unidadeId) {
        return estoqueRepository
                .findByUnidadeId(unidadeId)
                .stream()
                .map(EstoqueResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EstoqueResponse> buscarItensAbaixoDoMinimo(
            Long unidadeId) {
        return estoqueRepository
                .findItensAbaixoDoMinimo(unidadeId)
                .stream()
                .map(EstoqueResponse::from)
                .toList();
    }

    private Estoque buscarOuErro(Long unidadeId, Long produtoId) {
        return estoqueRepository
                .findByUnidadeIdAndProdutoId(unidadeId, produtoId)
                .orElseThrow(() -> new EstoqueNaoEncontradoException(
                        unidadeId, produtoId));
    }
}