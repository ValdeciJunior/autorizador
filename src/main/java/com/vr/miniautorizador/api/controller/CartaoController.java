package com.vr.miniautorizador.api.controller;

import com.vr.miniautorizador.api.dto.CartaoRequest;
import com.vr.miniautorizador.api.dto.RecargaRequest;
import com.vr.miniautorizador.api.mapper.CartaoMapper;
import com.vr.miniautorizador.domain.model.Cartao;
import com.vr.miniautorizador.domain.service.CartaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cartoes")
@Tag(name = "Cartões", description = "Endpoints para criação e consulta de saldo de cartões")
public class CartaoController {

    private final CartaoService cartaoService;

    public CartaoController(CartaoService cartaoService) {
        this.cartaoService = cartaoService;
    }

    @PostMapping
    @Operation(summary = "Criar um novo cartão com saldo inicial de R$ 500,00")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cartão criado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Cartão já existente no sistema")
    })
    public ResponseEntity<CartaoRequest> criarCartao(@Valid @RequestBody CartaoRequest request) {
        CartaoRequest novoCartaoDto = cartaoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCartaoDto);
    }

    @GetMapping("/{numeroCartao}")
    @Operation(summary = "Obter o saldo de um cartão existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saldo retornado com sucesso"),
            @ApiResponse(responseCode = "442", description = "Cartão não encontrado")
    })
    public ResponseEntity<BigDecimal> obterSaldo(@PathVariable String numeroCartao) {
        BigDecimal saldo = cartaoService.obterSaldo(numeroCartao);
        return ResponseEntity.ok(saldo);
    }

    @PostMapping("/{numeroCartao}/recarga")
    @Operation(summary = "Realigar recarga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recarga efetuada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Cartão inexistente para recarga")
    })
    public ResponseEntity<String> realizarRegarga(@Valid @PathVariable String numeroCartao, @RequestBody RecargaRequest request){
        cartaoService.realizarRegarga(numeroCartao, request);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}