package com.vr.miniautorizador.api.controller;

import com.vr.miniautorizador.domain.exception.RegraAutorizacaoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura as rejeições do motor de autorização sem 'if' e responde com Status 422 e texto puro
    @ExceptionHandler(RegraAutorizacaoException.class)
    public ResponseEntity<String> handleRegraAutorizacao(RegraAutorizacaoException ex) {
        return ResponseEntity.unprocessableEntity().body(ex.getMessage());
    }

    // Captura erros de validação sintática do Spring Validation (como formato ou tamanho de campo)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex) {
        // Para o autorizador, se o payload estiver malformado, devolvemos 400 Bad Request
        return ResponseEntity.badRequest().body("PAYLOAD_INVALIDO");
    }
}