package com.raizes.nordeste.api.controller;


import com.raizes.nordeste.api.dto.request.AtualizarStatusRequest;
import com.raizes.nordeste.api.dto.request.CancelarPedidoRequest;
import com.raizes.nordeste.api.dto.request.CriarPedidoRequest;
import com.raizes.nordeste.api.dto.response.AtualizarStatusResponse;
import com.raizes.nordeste.api.dto.response.PedidoResponse;
import com.raizes.nordeste.application.pedido.PedidoService;
import com.raizes.nordeste.domain.enums.CanalPedido;
import com.raizes.nordeste.domain.enums.StatusPedido;
import com.raizes.nordeste.domain.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Gestao de pedidos da rede")
@SecurityRequirement(name = "bearerAuth")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE','ATENDENTE')")
    @Operation(summary = "Cria um novo pedido")
    public ResponseEntity<PedidoResponse> criar(
            @RequestBody @Valid CriarPedidoRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuario = pedidoService
                .buscarUsuarioPorEmail(userDetails.getUsername());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoService.criarPedido(req, usuario));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE','ADMIN','ATENDENTE','COZINHA')")
    @Operation(summary = "Lista pedidos com filtros opcionais")
    public ResponseEntity<Page<PedidoResponse>> listar(
            @RequestParam(required = false) CanalPedido canalPedido,
            @RequestParam(required = false) StatusPedido status,
            @RequestParam(required = false) Long unidadeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit) {

        return ResponseEntity.ok(
                pedidoService.listar(
                        canalPedido, status, unidadeId,
                        PageRequest.of(page, limit)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um pedido pelo ID")
    public ResponseEntity<PedidoResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('COZINHA','ATENDENTE','GERENTE','ADMIN')")
    @Operation(summary = "Atualiza o status do pedido")
    public ResponseEntity<AtualizarStatusResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarStatusRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuario = pedidoService
                .buscarUsuarioPorEmail(userDetails.getUsername());

        return ResponseEntity.ok(
                pedidoService.atualizarStatus(id, req.status(), usuario));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE','ATENDENTE','GERENTE','ADMIN')")
    @Operation(summary = "Cancela um pedido")
    public ResponseEntity<PedidoResponse> cancelar(
            @PathVariable Long id,
            @RequestBody @Valid CancelarPedidoRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuario = pedidoService
                .buscarUsuarioPorEmail(userDetails.getUsername());

        return ResponseEntity.ok(
                pedidoService.cancelar(id, req.motivo(), usuario));
    }
}