package com.vr.miniautorizador.api.controller;

import com.vr.miniautorizador.api.dto.TransacaoRequest;
import com.vr.miniautorizador.domain.service.AutorizadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacoes")
@Tag(name = "Transações", description = "Endpoint para autorização de transações financeiras")
public class TransacaoController {

    private final AutorizadorService autorizadorService;

    public TransacaoController(AutorizadorService autorizadorService) {
        this.autorizadorService = autorizadorService;
    }

    @PostMapping
    @Operation(summary = "Realizar uma transação de débito no cartão")
    public ResponseEntity<String> realizarTransacao(@Valid @RequestBody TransacaoRequest request) {
        autorizadorService.realizarTransacao(request.numeroCartao(), request.senhaCartao(), request.valor(), request.estabelecimentoId());
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }

    @PostMapping("/{id}/estorno")
    @Operation(summary = "Realizar estorno de transacao")
    public ResponseEntity<String> realizarEstorno(@PathVariable Long id){
        autorizadorService.realizarEstorno(id);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}