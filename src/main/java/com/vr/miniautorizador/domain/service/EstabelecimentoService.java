package com.vr.miniautorizador.domain.service;

import com.vr.miniautorizador.domain.exception.RegraAutorizacaoException;
import com.vr.miniautorizador.domain.model.Estabelecimento;
import com.vr.miniautorizador.domain.repository.EstabelecimentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstabelecimentoService {

    EstabelecimentoRepository estabelecimentoRepository;

    EstabelecimentoService(EstabelecimentoRepository estabelecimentoRepository) {
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    @Transactional
    Estabelecimento findById(Long estabelecimentoId) {
        return estabelecimentoRepository.findById(estabelecimentoId)
                .orElseThrow(() -> new RegraAutorizacaoException("ESTABELECIMENTO_INEXISTENTE"));
    }
}
