package com.challenge.commission.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComissaoServiceTest {

    private final ComissaoService service = new ComissaoService();

    @Test
    void vendaAbaixoDe100NaoGeraComissao() {
        assertEquals(new BigDecimal("0.00"), service.calcularComissao(new BigDecimal("90.75")));
    }

    @Test
    void vendaAbaixoDe500GeraUmPorcento() {
        assertEquals(new BigDecimal("2.50"), service.calcularComissao(new BigDecimal("250.30")));
        assertEquals(new BigDecimal("0.01"), service.percentual(new BigDecimal("250.30")));
    }

    @Test
    void vendaAPartirDe500GeraCincoPorcento() {
        assertEquals(new BigDecimal("25.00"), service.calcularComissao(new BigDecimal("500.00")));
        assertEquals(new BigDecimal("60.03"), service.calcularComissao(new BigDecimal("1200.50")));
    }
}
