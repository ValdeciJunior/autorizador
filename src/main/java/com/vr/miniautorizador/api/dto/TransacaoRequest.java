package com.vr.miniautorizador.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransacaoRequest(
        @Schema(description = "Número do cartão com 16 dígitos", example = "6543210987654321")
        @NotBlank(message = "O número do cartão é obrigatório")
        String numeroCartao,

        @Schema(description = "Senha numérica do cartão", example = "1234")
        @NotBlank(message = "A senha do cartão é obrigatória")
        String senhaCartao,

        @Schema(description = "Valor a ser debitado", example = "10.00")
        @NotNull(message = "O valor da transação é obrigatório")
        @Min(value = 0, message = "O valor da transação deve ser maior que zero")
        BigDecimal valor,

        @Schema(description = "ID do estabelecimento onde a compra está sendo feita", example = "1")
        @NotNull(message = "O estabelecimento é obrigatório")
        Long estabelecimentoId
) {}