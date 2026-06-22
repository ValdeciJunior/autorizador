package com.vr.miniautorizador.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RecargaRequest(

        @Schema(description = "Valor a ser creditado", example = "10.00")
        @NotNull(message = "O valor da transação é obrigatório")
        @Min(value = 0, message = "O valor da transação deve ser maior que zero")
        BigDecimal valor
) {


}
