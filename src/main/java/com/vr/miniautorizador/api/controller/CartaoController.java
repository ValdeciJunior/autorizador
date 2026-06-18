package com.vr.miniautorizador.api.controller;

import com.vr.miniautorizador.api.dto.CartaoRequest;
import com.vr.miniautorizador.api.mapper.CartaoMapper;
import com.vr.miniautorizador.domain.model.Cartao;
import com.vr.miniautorizador.domain.repository.CartaoRepository;
import io.swagger.v3.oas.annotations.Operation;
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

    private final CartaoRepository cartaoRepository;
    private final CartaoMapper cartaoMapper;

    public CartaoController(CartaoRepository cartaoRepository, CartaoMapper cartaoMapper) {
        this.cartaoRepository = cartaoRepository;
        this.cartaoMapper = cartaoMapper;
    }


    //
    @PostMapping
    @Operation(summary = "Criar um novo cartão com saldo inicial de R$ 500,00")
    public ResponseEntity<CartaoRequest> criarCartao(@Valid @RequestBody CartaoRequest request) {
        return cartaoRepository.findByNumeroCartao(request.numeroCartao())
                .map(cartaoExistente -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartaoMapper.toDto(cartaoExistente)))
                .orElseGet(() -> {
                    Cartao novoCartao = new Cartao(request.numeroCartao(), request.senha());
                    Cartao cartaoSalvo = cartaoRepository.save(novoCartao);
                    return ResponseEntity.status(HttpStatus.CREATED).body(cartaoMapper.toDto(cartaoSalvo));
                });
    }

    @GetMapping("/{numeroCartao}")
    @Operation(summary = "Obter o saldo de um cartão existente")
    public ResponseEntity<BigDecimal> obterSaldo(@PathVariable String numeroCartao) {
        return cartaoRepository.findByNumeroCartao(numeroCartao)
                .map(cartao -> ResponseEntity.ok(cartao.getSaldo()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}