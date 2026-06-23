package com.raizes.nordeste.application.pedido;

import com.raizes.nordeste.api.dto.request.CriarPedidoRequest;
import com.raizes.nordeste.api.dto.response.AtualizarStatusResponse;
import com.raizes.nordeste.api.dto.response.PedidoResponse;
import com.raizes.nordeste.api.exception.ProdutoIndisponivelException;
import com.raizes.nordeste.api.exception.RecursoNaoEncontradoException;
import com.raizes.nordeste.application.audit.AuditLogService;
import com.raizes.nordeste.application.estoque.EstoqueService;
import com.raizes.nordeste.application.pagamento.PagamentoService;
import com.raizes.nordeste.domain.enums.CanalPedido;
import com.raizes.nordeste.domain.enums.StatusPedido;
import com.raizes.nordeste.domain.model.CardapioUnidade;
import com.raizes.nordeste.domain.model.ItemPedido;
import com.raizes.nordeste.domain.model.Pedido;
import com.raizes.nordeste.domain.model.Unidade;
import com.raizes.nordeste.domain.model.Usuario;
import com.raizes.nordeste.infrastructure.repository.CardapioUnidadeRepository;
import com.raizes.nordeste.infrastructure.repository.PedidoRepository;
import com.raizes.nordeste.infrastructure.repository.UnidadeRepository;
import com.raizes.nordeste.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.raizes.nordeste.infrastructure.repository.PagamentoRepository;
import com.raizes.nordeste.api.dto.response.PagamentoResponse;
import com.raizes.nordeste.domain.enums.PerfilUsuario;
import org.springframework.security.access.AccessDeniedException;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UnidadeRepository unidadeRepository;
    private final CardapioUnidadeRepository cardapioRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstoqueService estoqueService;
    private final PagamentoService pagamentoService;
    private final AuditLogService auditLogService;
    private final PagamentoRepository pagamentoRepository;
    private final RegraDesconto regraDesconto;

    @Transactional
    public PedidoResponse criarPedido(CriarPedidoRequest req,
                                      Usuario cliente) {
        //records do java ja geram metodos getters sem o prefixo get usasse apenas req.unidadeId() ao inves de req.getUnidadeId()
        Unidade unidade = unidadeRepository
                .findByIdAndAtivoTrue(req.unidadeId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Unidade", req.unidadeId()));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setUnidade(unidade);
        pedido.setCanalPedido(req.canalPedido());

        List<ItemPedido> itens = new ArrayList<>(req.itens().stream()
                .map(itemReq -> {

                    CardapioUnidade cardapio = cardapioRepository
                            .findByUnidadeIdAndProdutoIdAndDisponivelTrue(
                                    unidade.getId(), itemReq.produtoId())
                            .orElseThrow(() ->
                                    new ProdutoIndisponivelException(
                                            itemReq.produtoId()));

                    estoqueService.validarDisponibilidade(
                            unidade.getId(),
                            itemReq.produtoId(),
                            itemReq.quantidade());

                    ItemPedido item = new ItemPedido();
                    item.setPedido(pedido);
                    item.setProduto(cardapio.getProduto());
                    item.setQuantidade(itemReq.quantidade());
                    item.setPrecoUnitario(cardapio.getPrecoLocal());
                    return item;
                }).toList());

        pedido.setItens(itens);
        pedido.calcularTotal();

        /* Ponto de extensao para regras de desconto/campanhas futuras
         A implementacao atual (RegraDesconto.SemDesconto) sempre retorna ZERO, entao o saldoPontos ainda nao precisa de integracao real com a Fidelidade
        Uma campanha futura poderia implementar RegraDesconto sem altera nenhuma linha deste service */
        BigDecimal desconto = regraDesconto.calcularDesconto(
                pedido.getTotal(), 0);
        pedido.setTotal(pedido.getTotal().subtract(desconto));

        pedidoRepository.save(pedido);

        auditLogService.registrar(
                cliente,
                "CRIAR_PEDIDO",
                "Pedido",
                pedido.getId());

        pagamentoService.processarMock(pedido, req.formaPagamento());

        Pedido pedidoAtualizado = pedidoRepository
                .findById(pedido.getId())
                .orElseThrow();

        //Busca o pagamento diretamente pelo pedidoId
        PagamentoResponse pagamentoResponse = pagamentoRepository
                .findByPedidoId(pedido.getId())
                .map(PagamentoResponse::from)
                .orElse(null);

        // Monta a resposta com o pagamento incluido junto
        return new PedidoResponse(
                pedidoAtualizado.getId(),
                pedidoAtualizado.getCanalPedido().name(),
                pedidoAtualizado.getStatus().name(),
                pedidoAtualizado.getTotal(),
                pedidoAtualizado.getItens().stream()
                        .map(com.raizes.nordeste.api.dto.response
                                .ItemPedidoResponse::from)
                        .toList(),
                pagamentoResponse,
                pedidoAtualizado.getCreatedAt()
        );

    }

    @Transactional(readOnly = true)
    public Page<PedidoResponse> listar(CanalPedido canal,
                                       StatusPedido status,
                                       Long unidadeId,
                                       Pageable pageable) {

        if (canal != null && status != null) {
            return pedidoRepository
                     .findByCanalPedidoAndStatus(canal, status, pageable)
                    .map(PedidoResponse::from);
        }
        if (canal != null) {
            return pedidoRepository
                    .findByCanalPedido(canal, pageable)
                     .map(PedidoResponse::from);
        }
        if (status != null) {
            return pedidoRepository
                    .findByStatus(status, pageable)
                    .map(PedidoResponse::from);
        }
        if (unidadeId != null) {
            return pedidoRepository
                    .findByUnidadeId(unidadeId, pageable)
                     .map(PedidoResponse::from);
        }

        return pedidoRepository
                 .findAll(pageable)
                .map(PedidoResponse::from);
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id, Usuario solicitante) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Pedido", id));

        if (solicitante.getPerfil() == PerfilUsuario.CLIENTE
                && !pedido.getCliente().getId().equals(solicitante.getId())) {
            throw new AccessDeniedException(
                    "Voce so pode consultar os proprios pedidos.");
        }

        return PedidoResponse.from(pedido);
    }

    @Transactional
    public AtualizarStatusResponse atualizarStatus(
            Long id,
            StatusPedido novoStatus,
            Usuario responsavel) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Pedido", id));

        StatusPedido statusAnterior = pedido.getStatus();

        pedido.atualizarStatus(novoStatus);
        pedido.setUpdatedAt(LocalDateTime.now());
        pedidoRepository.save(pedido);

        auditLogService.registrar(
                responsavel,
                "STATUS_ATUALIZADO",
                "Pedido",
                pedido.getId());

        return new AtualizarStatusResponse(
                pedido.getId(),
                statusAnterior.name(),
                novoStatus.name(),
                pedido.getUpdatedAt());
    }

    @Transactional
    public PedidoResponse cancelar(Long id,
                                   String motivo,
                                   Usuario responsavel) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Pedido", id));

        if (responsavel.getPerfil() == PerfilUsuario.CLIENTE
                && !pedido.getCliente().getId().equals(responsavel.getId())) {
            throw new AccessDeniedException(
                    "Voce so pode cancelar os proprios pedidos.");
        }

        pedido.cancelar();

        pedidoRepository.save(pedido);

        auditLogService.registrar(
                responsavel,
                "CANCELAR_PEDIDO",
                "Pedido",
                pedido.getId(),
                java.util.Map.of("motivo", motivo));

        return PedidoResponse.from(pedido);
    }

    @Transactional(readOnly = true)
    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository
                .findByEmailAndAtivoTrue(email)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuario", 0L));
    }
}