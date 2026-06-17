package com.raizes.nordeste.api.controller;


import com.raizes.nordeste.api.dto.request.EntradaEstoqueRequest;
import com.raizes.nordeste.api.dto.response.EstoqueResponse;
import com.raizes.nordeste.application.estoque.EstoqueService;
import com.raizes.nordeste.application.pedido.PedidoService;
import com.raizes.nordeste.domain.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/estoque")
@RequiredArgsConstructor
@Tag(name = "Estoque",
        description = "Controle de estoque por  unidade")
@SecurityRequirement(name = "bearerAuth")
public class EstoqueController {

    private final EstoqueService estoqueService;
    private final PedidoService pedidoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE','ADMIN','ATENDENTE')")
    @Operation(summary = "Consulta o estoque de uma unidade")
    public ResponseEntity<List<EstoqueResponse>> consultar(
            @RequestParam Long unidadeId,
            @RequestParam(required = false,
                    defaultValue = "false")
            Boolean abaixoMinimo) {

        if (Boolean.TRUE.equals(abaixoMinimo)) {
            return ResponseEntity.ok(
                    estoqueService.buscarItensAbaixoDoMinimo(unidadeId));
        }

        return ResponseEntity.ok(
                estoqueService.buscarPorUnidade(unidadeId));
    }

    @PostMapping("/entrada")
    @PreAuthorize("hasAnyRole('GERENTE','ADMIN')")
    @Operation(summary = "Registra entrada de mercadoria  no estoque")
    public ResponseEntity<EstoqueResponse> registrarEntrada(
            @RequestBody @Valid EntradaEstoqueRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuario = pedidoService
                .buscarUsuarioPorEmail(userDetails.getUsername());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(estoqueService.registrarEntrada(req, usuario));
    }
}