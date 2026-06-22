package com.vr.miniautorizador.domain.service;

import com.vr.miniautorizador.api.dto.CartaoRequest;
import com.vr.miniautorizador.api.dto.RecargaRequest;
import com.vr.miniautorizador.api.mapper.CartaoMapper;
import com.vr.miniautorizador.domain.exception.RegraAutorizacaoException;
import com.vr.miniautorizador.domain.model.Cartao;
import com.vr.miniautorizador.domain.repository.CartaoRepository;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartaoService {

    private final CartaoRepository cartaoRepository;

    CartaoService(CartaoRepository cartaoRepository){
        this.cartaoRepository = cartaoRepository;
    }

    public Cartao findByNumeroCartao(String numeroCartao) {
        return cartaoRepository.findByNumeroCartaoWithLock(numeroCartao)
                .orElseThrow(() -> new RegraAutorizacaoException("CARTAO_INEXISTENTE"));
    }

    @Transactional
    public Cartao save(Cartao cartao) {
        return cartaoRepository.save(cartao);
    }

    @Transactional
    public void realizarRegarga(@Valid String numeroCartao, RecargaRequest request) {
        Cartao cartao = this.findByNumeroCartao(numeroCartao);
        cartao.creditar(request.valor());
    }

    @Transactional
    public CartaoRequest criar(@Valid CartaoRequest request) {
        naoExisteCartao(request.numeroCartao());
        Cartao cartao = CartaoMapper.INSTANCE.toEntity(request);
        cartao.setSaldo(new BigDecimal(500));
        Cartao cartaoSalvo = cartaoRepository.save(cartao);
        return CartaoMapper.INSTANCE.toDto(cartaoSalvo);
    }

    public BigDecimal obterSaldo(String numeroCartao) {
        return cartaoRepository.findByNumeroCartao(numeroCartao)
                .map(Cartao::getSaldo)
                .orElseThrow(() -> new RegraAutorizacaoException("CARTAO_INEXISTENTE"));
    }

    private void naoExisteCartao(String numeroCartao) {
        cartaoRepository.findByNumeroCartao(numeroCartao)
                .ifPresent(cartao -> {
                    throw new RegraAutorizacaoException("CARTAO_JA_EXISTE");
                });
    }
}
