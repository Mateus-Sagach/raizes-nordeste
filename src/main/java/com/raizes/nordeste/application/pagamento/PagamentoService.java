package com.raizes.nordeste.application.pagamento;


import com.raizes.nordeste.api.dto.response.PagamentoResponse;
import com.raizes.nordeste.application.audit.AuditLogService;
import com.raizes.nordeste.application.estoque.EstoqueService;
import com.raizes.nordeste.application.fidelidade.FidelidadeService;
import com.raizes.nordeste.domain.enums.FormaPagamento;
import com.raizes.nordeste.domain.enums.StatusPagamento;
import com.raizes.nordeste.domain.enums.StatusPedido;
import com.raizes.nordeste.domain.model.Pagamento;
import com.raizes.nordeste.domain.model.Pedido;
import com.raizes.nordeste.infrastructure.gateway.PagamentoGatewayMock;
import com.raizes.nordeste.infrastructure.repository.PagamentoRepository;
import com.raizes.nordeste.infrastructure.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final PedidoRepository pedidoRepository;
    private final EstoqueService estoqueService;
    private final FidelidadeService fidelidadeService;
    private final AuditLogService auditLogService;
    private final PagamentoGatewayMock gateway;

    @Transactional
    public PagamentoResponse processarMock(Pedido pedido,
                                           FormaPagamento forma) {
        if (pagamentoRepository.existsByPedidoId(pedido.getId())) {
            log.warn("Pagamento ja existe para o pedido {}",
                    pedido.getId());
            return PagamentoResponse.from(
                    pagamentoRepository
                            .findByPedidoId(pedido.getId())
                            .get());
        }

        PagamentoGatewayMock.GatewayResponse resultado =
                gateway.processar(
                        pedido.getId(),
                        pedido.getTotal(),
                        forma.name());

        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setFormaPagamento(forma);
        pagamento.setValor(pedido.getTotal());
        pagamento.setStatus(resultado.status());
        pagamento.setGatewayRef(resultado.gatewayRef());
        pagamentoRepository.save(pagamento);

        if (resultado.status() == StatusPagamento.APROVADO) {
            onPagamentoAprovado(pedido);
        } else {
            log.warn("Pagamento recusado para o pedido {}",
                    pedido.getId());
            auditLogService.registrar(
                    pedido.getCliente(),
                    "PAGAMENTO_RECUSADO",
                    "Pedido",
                    pedido.getId());
        }

        return PagamentoResponse.from(pagamento);
    }

    private void onPagamentoAprovado(Pedido pedido) {

        pedido.atualizarStatus(StatusPedido.EM_PREPARACAO);
        pedidoRepository.save(pedido);

        pedido.getItens().forEach(item ->
                estoqueService.decrementar(
                        pedido.getUnidade().getId(),
                        item.getProduto().getId(),
                        item.getQuantidade()));

        fidelidadeService.acumular(pedido);

        auditLogService.registrar(
                pedido.getCliente(),
                "PAGAMENTO_APROVADO",
                "Pedido",
                pedido.getId());

        log.info("Pedido {} aprovado e em preparacao",
                pedido.getId());
    }
}