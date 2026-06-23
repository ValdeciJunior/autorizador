package com.vr.miniautorizador.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vr.miniautorizador.ai.AnaliseFraudeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analises")
@Tag(name = "Antifraude (IA)", description = "Integração experimental com Llama para análise de risco")
public class FraudeController {

    private final AnaliseFraudeService analiseFraudeService;

    public FraudeController(AnaliseFraudeService analiseFraudeService) {
        this.analiseFraudeService = analiseFraudeService;
    }

    @GetMapping("/detectar")
    @Operation(summary = "Enviar dados de comportamento para o Llama analisar")
    public String testarAnalise() throws JsonProcessingException {
        return analiseFraudeService.analisarComportamento();
    }
}
