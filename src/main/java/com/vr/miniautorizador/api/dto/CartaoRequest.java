package com.vr.miniautorizador.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CartaoRequest(
        @NotBlank(message = "O número do cartão é obrigatório")
        @Size(min = 16, max = 16, message = "O número do cartão deve ter exatamente 16 dígitos")
        @Pattern(regexp = "\\d+", message = "O número do cartão deve conter apenas dígitos numéricos")
        String numeroCartao,

        @NotBlank(message = "A senha do cartão é obrigatória")
        @Pattern(regexp = "\\d+", message = "A senha deve conter apenas números")
        String senha
) {}