package com.vr.miniautorizador.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransacaoRequest(
        @NotBlank(message = "O número do cartão é obrigatório")
        String numeroCartao,

        @NotBlank(message = "A senha do cartão é obrigatória")
        String senhaCartao,

        @NotNull(message = "O valor da transação é obrigatório")
        @Min(value = 0, message = "O valor da transação deve ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "O estabelecimento é obrigatório")
        Long estabelecimentoId
) {}