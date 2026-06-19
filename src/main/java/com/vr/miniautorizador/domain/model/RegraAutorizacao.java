package com.vr.miniautorizador.domain.model;

import com.vr.miniautorizador.domain.exception.RegraAutorizacaoException;

import java.math.BigDecimal;
import java.util.Optional;

public enum RegraAutorizacao {
    VALIDAR_SENHA {
        @Override
        public void avaliar(Cartao cartao, String senha, BigDecimal valor) {
            // Usamos operador de curto-circuito booleano para disparar a exceção sem usar 'if'
            boolean senhaInvalida = !cartao.getSenha().equals(senha);

            //Se a senha estiver inválida, lançamos a exceção, caso contrário a requisição continua no caminho feliz
            boolean deveraBloquear = senhaInvalida && lançarExcecao("SENHA_INVALIDA");
        }
    },

    VALIDAR_SALDO {
        @Override
        public void avaliar(Cartao cartao, String senha, BigDecimal valor) {
            //curto-circuito para verificar se o saldo é suficiente
            boolean saldoInsuficiente = cartao.getSaldo().compareTo(valor) < 0;

            //Se o saldo for insuficiante, lançamos a exceção, caso contrário a requisição continua no caminho feliz
            boolean deveraBloquear = saldoInsuficiente && lançarExcecao("SALDO_INSUFICIENTE");
        }
    };

    // Método abstrato implementado polimorficamente por cada constante
    // Ponto crucial para conseguirmos criar uma regra de negócio sem utilização dos 'ifs'
    public abstract void avaliar(Cartao cartao, String senha, BigDecimal valor);

    // Método utilitário funcional auxiliar para forçar o lançamento da exceção em expressões booleanas
    protected boolean lançarExcecao(String motivo) {
        throw new RegraAutorizacaoException(motivo);
    }
}