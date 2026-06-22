package com.vr.miniautorizador.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CartaoRequest(

        @Schema(description = "Número do cartão com 16 dígitos", example = "6543210987654321")
        @NotBlank(message = "O número do cartão é obrigatório")
        @Size(min = 16, max = 16, message = "O número do cartão deve ter exatamente 16 dígitos")
        @Pattern(regexp = "\\d+", message = "O número do cartão deve conter apenas dígitos numéricos")
        String numeroCartao,

        @Schema(description = "Senha numérica do cartão", example = "1234")
        @NotBlank(message = "A senha do cartão é obrigatória")
        @Pattern(regexp = "\\d+", message = "A senha deve conter apenas números")
        String senha
) {}