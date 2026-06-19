package com.vr.miniautorizador.domain.repository;

import com.vr.miniautorizador.domain.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    // No futuro adicionar um método de busca customizado por número de cartão para a AI Buscar o histórico.
}