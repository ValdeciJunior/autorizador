package com.vr.miniautorizador.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.miniautorizador.domain.model.Transacao;
import com.vr.miniautorizador.domain.service.AutorizadorService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnaliseFraudeService {

    private final ChatModel chatModel;
    private final AutorizadorService autorizadorService;
    private final ObjectMapper objectMapper;

    AnaliseFraudeService(ChatModel chatModel, AutorizadorService autorizadorService, ObjectMapper objectMapper){
        this.chatModel = chatModel;
        this.autorizadorService = autorizadorService;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public String analisarComportamento() throws JsonProcessingException {
        List<Transacao> transacoes = autorizadorService.todasTransacoes();
        return analisarComportamento(objectMapper.writeValueAsString(transacoes));
    }

    public String analisarComportamento(String json) {
        System.out.println(json);

        String instrucoesDoSistema = """
            Você é um motor antifraude especialista em cartões de benefício.
            Analise o JSON de transações enviado pelo usuário e retorne um parecer direto e objetivo.
            Informando qual estabelecimento é suspeito de estar comentendo fraudes
            por exemplo: Estabelecimentos que comprar saldo de vr.
            Infomando se um cartão está suspeito de ter sido clonado. Por exemplo:
            Compras suspeitas ou comportamento diferente do habitual. 
            """;

        Message systemMessage = new UserMessage(instrucoesDoSistema);
        Message userMessage = new UserMessage(json);

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        return chatModel.call(prompt).getResult().getOutput().getContent();
    }
}