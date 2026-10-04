package com.challenge.commission.repository;

import com.challenge.commission.dto.Venda;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VendaRepositoryTest {

    private VendaRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        repository = new VendaRepository(new ObjectMapper());
        repository.carregar();
    }

    @Test
    void carregaVendasDoArquivoNaInicializacao() {
        assertEquals(36, repository.quantidade());
        assertEquals("João Silva", repository.listar().get(0).getVendedor());
    }

    @Test
    void adicionarIncluiVendasNaListaEDevolveAsAdicionadas() {
        int antes = repository.quantidade();
        List<Venda> novas = List.of(
                new Venda("Novo Vendedor", new BigDecimal("700.00")),
                new Venda("Novo Vendedor", new BigDecimal("120.00"))
        );

        List<Venda> adicionadas = repository.adicionar(novas);

        assertEquals(2, adicionadas.size());
        assertEquals(antes + 2, repository.quantidade());
        assertEquals(2, repository.listarPorVendedor("Novo Vendedor").size());
    }

    @Test
    void listarPorVendedorIgnoraMaiusculasEMinusculas() {
        List<Venda> vendas = repository.listarPorVendedor("ana lima");

        assertEquals(9, vendas.size());
        assertTrue(vendas.stream().allMatch(v -> v.getVendedor().equals("Ana Lima")));
    }

    @Test
    void listarDevolveCopiaImutavel() {
        List<Venda> lista = repository.listar();

        org.junit.jupiter.api.Assertions.assertThrows(UnsupportedOperationException.class,
                () -> lista.add(new Venda("X", BigDecimal.ONE)));
    }
}
