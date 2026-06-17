package com.raizes.nordeste.api.controller;

import com.raizes.nordeste.api.dto.response.CardapioResponse;
import com.raizes.nordeste.application.cardapio.CardapioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Cardapio", description = "Consulta de   produtos disponiveis por  unidade")
public class CardapioController {

    private final CardapioService cardapioService;

    @GetMapping("/unidades/{id}/cardapio")
    @Operation(summary = "Consulta o cardapio disponivel de uma unidade")
    public ResponseEntity<List<CardapioResponse>> consultarPorUnidade(
            @PathVariable Long id) {


        return ResponseEntity.ok(cardapioService.consultarPorUnidade(id));
    }
}