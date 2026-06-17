package com.vr.miniautorizador.domain.repository;

import com.vr.miniautorizador.domain.model.Cartao;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {

    // Busca padrão de leitura comum (usada na consulta de saldo)
    Optional<Cartao> findByNumeroCartao(String numeroCartao);

    // Busca com Trava de Escrita Atômica (usada estritamente no fluxo de autorização)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Cartao c WHERE c.numeroCartao = :numeroCartao")
    Optional<Cartao> findByNumeroCartaoWithLock(String numeroCartao);
}