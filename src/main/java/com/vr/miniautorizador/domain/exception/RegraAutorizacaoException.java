package com.vr.miniautorizador.domain.exception;

public class RegraAutorizacaoException extends RuntimeException {

    public RegraAutorizacaoException(String motivoRejeicao) {
        super(motivoRejeicao);
    }
}