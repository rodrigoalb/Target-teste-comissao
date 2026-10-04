package com.challenge.commission.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testa o fluxo completo: POST /api/vendas adiciona em memória e GET /api/comissoes reflete a nova venda.
 */
@SpringBootTest
@AutoConfigureMockMvc
class VendaFluxoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void vendaPostadaApareceNoCalculoDeComissao() throws Exception {
        mockMvc.perform(get("/api/comissoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].vendedor", not(hasItem("Vendedor Teste"))));

        mockMvc.perform(post("/api/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "vendas": [
                                  { "vendedor": "Vendedor Teste", "valor": 1000.00 },
                                  { "vendedor": "Vendedor Teste", "valor": 50.00 }
                                ] }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].vendedor").value("Vendedor Teste"));

        mockMvc.perform(get("/api/comissoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].vendedor", hasItem("Vendedor Teste")));

        mockMvc.perform(get("/api/comissoes/Vendedor Teste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendedor").value("Vendedor Teste"))
                .andExpect(jsonPath("$.quantidadeVendas").value(2))
                .andExpect(jsonPath("$.totalVendas").value(1050.00))
                .andExpect(jsonPath("$.totalComissao").value(50.00));

        mockMvc.perform(get("/api/vendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].vendedor", hasItem("Vendedor Teste")));
    }

    @Test
    void postComissoesNaoPersisteNada() throws Exception {
        mockMvc.perform(post("/api/comissoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "vendas": [ { "vendedor": "Simulado", "valor": 800.00 } ] }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].vendedor").value("Simulado"))
                .andExpect(jsonPath("$[0].totalComissao").value(40.00));

        mockMvc.perform(get("/api/comissoes"))
                .andExpect(jsonPath("$[*].vendedor", not(hasItem("Simulado"))));
    }

    @Test
    void vendaInvalidaRetorna400() throws Exception {
        mockMvc.perform(post("/api/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "vendas": [ { "vendedor": "", "valor": -5 } ] }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Dados inválidos"))
                .andExpect(jsonPath("$.detalhes", hasSize(2)));

        mockMvc.perform(post("/api/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"vendas\": [] }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void vendedorInexistenteRetorna404() throws Exception {
        mockMvc.perform(get("/api/comissoes/Nao Existe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Vendedor não encontrado: Nao Existe"));
    }
}
