package com.vr.miniautorizador.domain.service;

import com.vr.miniautorizador.domain.model.Cartao;
import com.vr.miniautorizador.domain.model.RegraAutorizacao;
import com.vr.miniautorizador.domain.repository.CartaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@Service
public class AutorizadorService {

    private final CartaoRepository cartaoRepository;

    // Construtor explícito para Injeção de Dependência sem Lombok
    public AutorizadorService(CartaoRepository cartaoRepository) {
        this.cartaoRepository = cartaoRepository;
    }

    @Transactional
    public void autorizarTransacao(String numeroCartao, String senha, BigDecimal valor) {
        Optional<Cartao> cartaoOpt = cartaoRepository.findByNumeroCartaoWithLock(numeroCartao);

        Arrays.stream(RegraAutorizacao.values())
                .forEach(regra -> regra.avaliar(cartaoOpt, senha, valor));

        Cartao cartao = cartaoOpt.get();
        cartao.debitar(valor);
    }
}