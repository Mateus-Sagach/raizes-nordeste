package com.raizes.nordeste.application.fidelidade;


import com.raizes.nordeste.api.dto.response.FidelidadeResponse;
import com.raizes.nordeste.api.exception.FidelidadeNaoEncontradaException;
import com.raizes.nordeste.api.exception.SaldoInsuficienteException;
import com.raizes.nordeste.domain.enums.TipoTransacao;
import com.raizes.nordeste.domain.model.Fidelidade;
import com.raizes.nordeste.domain.model.Pedido;
import com.raizes.nordeste.domain.model.TransacaoPontos;
import com.raizes.nordeste.domain.model.Usuario;
import com.raizes.nordeste.infrastructure.repository.FidelidadeRepository;
import com.raizes.nordeste.infrastructure.repository.TransacaoPontosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FidelidadeService {

    private final FidelidadeRepository fidelidadeRepository;
    private final TransacaoPontosRepository transacaoRepository;

    private static final int PONTOS_POR_REAL = 1; //caso mude a quantidade de ponto que ganha por real gasto alterar aqui

    @Transactional
    public void acumular(Pedido pedido) {

        if (!pedido.getCliente().getConsentimentoLgpd()) {
            return; //encerra o metodo caso nao haja o consentimento sobre a lgpd
        }

        int pontos = pedido.getTotal().intValue() * PONTOS_POR_REAL;

        if (pontos <= 0) return;

        Fidelidade fidelidade = fidelidadeRepository
                .findByClienteId(pedido.getCliente().getId())
                .orElseGet(() -> criarFidelidade(pedido.getCliente()));// se nao encontrou um progrmama de fidelidade para o clinte cria  um novo

        fidelidade.acumularPontos(pontos);
        fidelidadeRepository.save(fidelidade);

        TransacaoPontos tx = new TransacaoPontos();
        tx.setFidelidade(fidelidade);
        tx.setPedido(pedido);
        tx.setTipo(TipoTransacao.ACUMULO);
        tx.setPontos(pontos);
        tx.setDescricao("Pedido #" + pedido.getId());
        transacaoRepository.save(tx);
    }

    @Transactional
    public void resgatar(Long clienteId, int pontos, Long pedidoId) {

        Fidelidade fidelidade = fidelidadeRepository
                .findByClienteId(clienteId)
                .orElseThrow(() ->
                        new FidelidadeNaoEncontradaException(clienteId));

        if (fidelidade.getSaldoPontos() < pontos) {
            throw new SaldoInsuficienteException(
                    fidelidade.getSaldoPontos(), pontos);
        }

        fidelidade.resgatarPontos(pontos);
        fidelidadeRepository.save(fidelidade);

        TransacaoPontos tx = new TransacaoPontos();
        tx.setFidelidade(fidelidade);
        tx.setTipo(TipoTransacao.RESGATE);
        tx.setPontos(-pontos);
        tx.setDescricao("Resgate no pedido #" + pedidoId);
        transacaoRepository.save(tx);
    }

    @Transactional(readOnly = true)
    public FidelidadeResponse buscarPorCliente(Long clienteId) {
        Fidelidade fidelidade = fidelidadeRepository
                .findByClienteId(clienteId)
                .orElseThrow(() ->
                        new FidelidadeNaoEncontradaException(clienteId));

        // Converte para DTO aqui dentro, enquanto a sessao ainda esta aberta
        return FidelidadeResponse.from(fidelidade);
    }

    @Transactional(readOnly = true)
    public java.util.Map<String, Object> buscarHistorico(
            Long clienteId, int page, int limit) {

        Fidelidade fidelidade = fidelidadeRepository
                .findByClienteId(clienteId)
                .orElseThrow(() ->
                        new FidelidadeNaoEncontradaException(clienteId));

        Page<TransacaoPontos> historico = transacaoRepository
                .findByFidelidadeId(
                        fidelidade.getId(),
                        PageRequest.of(page, limit));

        // Converte tudo aqui dentro enquanto a sessao esta aberta
        return java.util.Map.of(
                "saldoAtual", fidelidade.getSaldoPontos(),
                "totalPaginas", historico.getTotalPages(),
                "totalItens", historico.getTotalElements(),
                "transacoes", historico.getContent().stream()
                        .map(tx -> java.util.Map.of(
                                "tipo", tx.getTipo().name(),
                                "pontos", tx.getPontos(),
                                "descricao", tx.getDescricao() != null
                                        ? tx.getDescricao() : "",
                                "criadoEm", tx.getCreatedAt().toString()
                        )).toList()
        );
    }

    private Fidelidade criarFidelidade(Usuario cliente) {
        Fidelidade f = new Fidelidade();
        f.setCliente(cliente);
        f.setSaldoPontos(0);
        f.setTotalAcumulado(0);
        return fidelidadeRepository.save(f);
    }
}