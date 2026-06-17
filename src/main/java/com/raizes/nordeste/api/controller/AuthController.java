package com.raizes.nordeste.api.controller;


import com.raizes.nordeste.api.dto.request.CriarUsuarioRequest;
import com.raizes.nordeste.api.dto.request.LoginRequest;
import com.raizes.nordeste.api.dto.response.LoginResponse;
import com.raizes.nordeste.api.dto.response.UsuarioResponse;
import com.raizes.nordeste.application.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticacao",
        description = "Login, cadastro e  perfil do usuario")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Autentica o usuario e retorna o token JWT")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest req) {

        return ResponseEntity.ok(
                authService.login(req.email(), req.senha()));
    }

    @PostMapping("/cadastro")
    @Operation(summary = "Cadastra  um novo usuario")
    public ResponseEntity<UsuarioResponse> cadastrar(
            @RequestBody @Valid CriarUsuarioRequest req) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.cadastrar(req));
    }

    @GetMapping("/me")
    @Operation(summary = "Retorna os dados do usuario  autenticado")
    public ResponseEntity<UsuarioResponse> meuPerfil(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                authService.buscarPerfil(userDetails.getUsername()));
    }
}