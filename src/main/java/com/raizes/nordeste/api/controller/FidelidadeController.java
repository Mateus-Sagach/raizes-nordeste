package com.raizes.nordeste.api.controller;


import com.raizes.nordeste.api.dto.response.FidelidadeResponse;
import com.raizes.nordeste.api.dto.response.UsuarioResponse;
import com.raizes.nordeste.application.fidelidade.FidelidadeService;
import com.raizes.nordeste.application.pedido.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fidelidade")
@RequiredArgsConstructor
@Tag(name = "Fidelidade",
        description = "Programa de pontos do cliente")
@SecurityRequirement(name = "bearerAuth")
public class FidelidadeController {

    private final FidelidadeService fidelidadeService;
    private final PedidoService pedidoService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Consulta o saldo de pontos do cliente")
    public ResponseEntity<FidelidadeResponse> consultarSaldo(
            @AuthenticationPrincipal UserDetails userDetails) {

        Long clienteId = pedidoService
                .buscarUsuarioPorEmail(userDetails.getUsername())
                .getId();

        return ResponseEntity.ok(
                fidelidadeService.buscarPorCliente(clienteId));
    }

    @GetMapping("/me/historico")
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Retorna o historico de transacoes de pontos")
    public ResponseEntity<java.util.Map<String, Object>> historico(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit) {

        Long clienteId = pedidoService
                .buscarUsuarioPorEmail(userDetails.getUsername())
                .getId();

        return ResponseEntity.ok(
                fidelidadeService.buscarHistorico(clienteId, page, limit));
    }


}