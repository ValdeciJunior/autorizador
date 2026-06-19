package com.vr.miniautorizador.domain.service;

import com.vr.miniautorizador.domain.exception.RegraAutorizacaoException;
import com.vr.miniautorizador.domain.model.Cartao;
import com.vr.miniautorizador.domain.repository.CartaoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartaoService {

    private final CartaoRepository cartaoRepository;

    CartaoService(CartaoRepository cartaoRepository){
        this.cartaoRepository = cartaoRepository;
    }

    @Transactional
    public Cartao findByNumeroCartao(String numeroCartao) {
        return cartaoRepository.findByNumeroCartaoWithLock(numeroCartao)
                .orElseThrow(() -> new RegraAutorizacaoException("CARTAO_INEXISTENTE"));
    }

    @Transactional
    public void save(Cartao cartao) {
        cartaoRepository.save(cartao);
    }
}
