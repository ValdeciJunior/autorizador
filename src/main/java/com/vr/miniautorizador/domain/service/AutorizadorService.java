package com.vr.miniautorizador.domain.service;

import com.vr.miniautorizador.domain.exception.RegraAutorizacaoException;
import com.vr.miniautorizador.domain.model.*;
import com.vr.miniautorizador.domain.repository.EstabelecimentoRepository;
import com.vr.miniautorizador.domain.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@Service
public class AutorizadorService {

    private final CartaoService cartaoService;
    private final EstabelecimentoService estabelecimentoService;
    private final TransacaoRepository transacaoRepository;

    public AutorizadorService(CartaoService cartaoService, EstabelecimentoService estabelecimentoService,
                              TransacaoRepository transacaoRepository) {
        this.cartaoService = cartaoService;
        this.estabelecimentoService = estabelecimentoService;
        this.transacaoRepository = transacaoRepository;
    }

    public void realizarTransacao(String numeroCartao, String senha, BigDecimal valor, Long estabelecimentoId) {
        Cartao cartao = cartaoService.findByNumeroCartao(numeroCartao);
        Estabelecimento estabelecimento = estabelecimentoService.findById(estabelecimentoId);

        try {
            Arrays.stream(RegraAutorizacao.values())
                    .forEach(regra -> regra.avaliar(cartao, senha, valor));

            Transacao transacaoAprovada = new Transacao(null, cartao, estabelecimento, valor, StatusTransacao.APROVADA, null);
            transacaoRepository.save(transacaoAprovada);

            cartao.debitar(valor);
            cartaoService.save(cartao);

        } catch (RegraAutorizacaoException ex) {
            Transacao transacaoRecusada = new Transacao(null, cartao, estabelecimento, valor, StatusTransacao.RECUSADA, ex.getMessage());
            transacaoRepository.save(transacaoRecusada);
            // Relança a exceção o Controller devolver o HTTP 422
            throw ex;
        }
    }

    public void realizarEstorno(Long id){
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new RegraAutorizacaoException("TRANSACAO_INEXISTENTE"));

        Transacao transacaoEstorno = new Transacao(null,
                transacao.getCartao(),
                transacao.getEstabelecimento(),
                transacao.getValor(),
                StatusTransacao.ESTORNADA,
                "estorno da transacao: "+transacao.getId());

        transacaoRepository.save(transacaoEstorno);

        Cartao cartao = cartaoService.findByNumeroCartao(transacao.getCartao().getNumeroCartao());
        cartao.creditar(transacao.getValor());
        cartaoService.save(cartao);

    }
}