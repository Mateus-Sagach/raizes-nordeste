package com.raizes.nordeste.api.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice(basePackages = "com.raizes.nordeste.api.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<ErroResponse.DetalheErro> detalhes = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> new ErroResponse.DetalheErro(
                        e.getField(), e.getDefaultMessage()))
                .toList();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErroResponse.of(
                        "ERRO_VALIDACAO",
                        "Um ou mais campos estao invalidos.",
                        detalhes,
                        request.getRequestURI()));
    }

    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ResponseEntity<ErroResponse> handleEstoque(
            EstoqueInsuficienteException ex,
            HttpServletRequest request) {

        List<ErroResponse.DetalheErro> detalhes = List.of(
                new ErroResponse.DetalheErro(
                        "quantidade",
                        "Disponivel: " + ex.getDisponivel()));

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErroResponse.of(
                        "ESTOQUE_INSUFICIENTE",
                        ex.getMessage(),
                        detalhes,
                        request.getRequestURI()));
    }

    @ExceptionHandler(TransicaoStatusInvalidaException.class)
    public ResponseEntity<ErroResponse> handleTransicao(
            TransicaoStatusInvalidaException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErroResponse.of(
                        "TRANSICAO_STATUS_INVALIDA",
                        ex.getMessage(),
                        request.getRequestURI()));
    }

    @ExceptionHandler(PedidoNaoCancelavelException.class)
    public ResponseEntity<ErroResponse> handleCancelamento(
            PedidoNaoCancelavelException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErroResponse.of(
                        "PEDIDO_NAO_CANCELAVEL",
                        ex.getMessage(),
                        request.getRequestURI()));
    }

    @ExceptionHandler(ProdutoIndisponivelException.class)
    public ResponseEntity<ErroResponse> handleProduto(
            ProdutoIndisponivelException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErroResponse.of(
                        "PRODUTO_INDISPONIVEL",
                        ex.getMessage(),
                        request.getRequestURI()));
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<ErroResponse> handleSaldo(
            SaldoInsuficienteException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErroResponse.of(
                        "SALDO_INSUFICIENTE",
                        ex.getMessage(),
                        request.getRequestURI()));
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> handleNaoEncontrado(
            RecursoNaoEncontradoException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErroResponse.of(
                        "RECURSO_NAO_ENCONTRADO",
                        ex.getMessage(),
                        request.getRequestURI()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroResponse> handleCredenciais(
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErroResponse.of(
                        "CREDENCIAIS_INVALIDAS",
                        "Email ou senha invalidos.",
                        request.getRequestURI()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroResponse> handleAcesso(
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErroResponse.of(
                        "ACESSO_NEGADO",
                        "Voce nao tem permissao para esta acao.",
                        request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGenerico(
            Exception ex,
            HttpServletRequest request) {

        // linha para resolver erro no swagger
        ex.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErroResponse.of(
                        "ERRO_INTERNO",
                        "Ocorreu um erro inesperado. Tente novamente.", //mensagem generica no lugar do ex.getMessage() para nao ter risco de expor infromações sensiveis
                        request.getRequestURI()));
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ErroResponse> handleEmailDuplicado(
            EmailJaCadastradoException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErroResponse.of(
                        "EMAIL_JA_CADASTRADO",
                        ex.getMessage(),
                        request.getRequestURI()));
    }
}