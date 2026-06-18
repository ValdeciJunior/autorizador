package com.vr.miniautorizador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.miniautorizador.api.dto.CartaoRequest;
import com.vr.miniautorizador.api.dto.TransacaoRequest;
import com.vr.miniautorizador.domain.repository.CartaoRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional // <-- Garante que tudo rodará dentro de um contexto transacional
@Rollback // <-- Força o rollback total das operações ao fim da execução
class MiniAutorizadorApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartaoRepository cartaoRepository;

    private static final String NUMERO_CARTAO_TESTE = "6549873025634501";
    private static final String SENHA_CORRETA = "1234";

    @Test
    @Order(1)
    void deveCriarCartaoComSucesso() throws Exception {
        // Tinha colocado só para os testes iniciais, pois utilizando o @Rollback ele limpa todas as alterações no banco
        // feitas pelas testes
        // cartaoRepository.deleteAll();

        CartaoRequest request = new CartaoRequest(NUMERO_CARTAO_TESTE, SENHA_CORRETA);

        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(request)));
    }

    @Test
    @Order(2)
    void deveVerificarSaldoDoCartaoRecemCriado() throws Exception {

        CartaoRequest request = new CartaoRequest(NUMERO_CARTAO_TESTE, SENHA_CORRETA);
        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(request)));

        mockMvc.perform(get("/cartoes/" + NUMERO_CARTAO_TESTE))
                .andExpect(status().isOk())
                .andExpect(content().string("500.00"));
    }

    @Test
    @Order(3)
    void deveRealizarDiversasTransacoesEApontarSaldoInsuficiente() throws Exception {

        CartaoRequest request = new CartaoRequest(NUMERO_CARTAO_TESTE, SENHA_CORRETA);
        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(request)));

        TransacaoRequest transacaoValida = new TransacaoRequest(NUMERO_CARTAO_TESTE, SENHA_CORRETA, new BigDecimal("200.00"));

        // Primeira transação de 200.00 (Saldo cai para 300.00)
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transacaoValida)))
                .andExpect(status().isCreated())
                .andExpect(content().string("OK"));

        // Segunda transação de 200.00 (Saldo cai para 100.00)
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transacaoValida)))
                .andExpect(status().isCreated())
                .andExpect(content().string("OK"));

        // Terceira transação de 200.00 -> Deve barrar por SALDO_INSUFICIENTE (só restam 100.00)
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transacaoValida)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("SALDO_INSUFICIENTE"));
    }

    @Test
    @Order(4)
    void deveRejeitarTransacaoComSenhaInvalida() throws Exception {
        CartaoRequest request = new CartaoRequest(NUMERO_CARTAO_TESTE, SENHA_CORRETA);
        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(request)));

        TransacaoRequest transacaoSenhaErrada = new TransacaoRequest(NUMERO_CARTAO_TESTE, "9999", new BigDecimal("10.00"));

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transacaoSenhaErrada)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("SENHA_INVALIDA"));
    }

    @Test
    @Order(5)
    void deveRejeitarTransacaoComCartaoInexistente() throws Exception {
        CartaoRequest request = new CartaoRequest(NUMERO_CARTAO_TESTE, SENHA_CORRETA);
        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(request)));

        TransacaoRequest transacaoCartaoInexistente = new TransacaoRequest("9999999999999999", "1234", new BigDecimal("10.00"));

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transacaoCartaoInexistente)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("CARTAO_INEXISTENTE"));
    }
}
