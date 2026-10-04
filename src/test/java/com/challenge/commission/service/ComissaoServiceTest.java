package com.challenge.commission.service;

import com.challenge.commission.dto.ComissaoVendedorResponse;
import com.challenge.commission.dto.Venda;
import com.challenge.commission.exception.VendedorNaoEncontradoException;
import com.challenge.commission.repository.VendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComissaoServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    private ComissaoService service;

    @BeforeEach
    void setUp() {
        service = new ComissaoService(vendaRepository);
    }

    @Test
    void vendaAbaixoDe100NaoGeraComissao() {
        assertEquals(new BigDecimal("0.00"), service.calcularComissao(new BigDecimal("90.75")));
        assertEquals(new BigDecimal("0.00"), service.calcularComissao(new BigDecimal("99.99")));
    }

    @Test
    void vendaAbaixoDe500GeraUmPorcento() {
        assertEquals(new BigDecimal("1.00"), service.calcularComissao(new BigDecimal("100.00")));
        assertEquals(new BigDecimal("2.50"), service.calcularComissao(new BigDecimal("250.30")));
        assertEquals(new BigDecimal("5.00"), service.calcularComissao(new BigDecimal("499.99")));
        assertEquals(new BigDecimal("0.01"), service.percentual(new BigDecimal("250.30")));
    }

    @Test
    void vendaAPartirDe500GeraCincoPorcento() {
        assertEquals(new BigDecimal("25.00"), service.calcularComissao(new BigDecimal("500.00")));
        assertEquals(new BigDecimal("60.03"), service.calcularComissao(new BigDecimal("1200.50")));
    }

    @Test
    void calcularAgrupaPorVendedorESomaTotais() {
        List<Venda> vendas = List.of(
                new Venda("Ana", new BigDecimal("1000.00")),
                new Venda("Bruno", new BigDecimal("50.00")),
                new Venda("Ana", new BigDecimal("200.00"))
        );

        List<ComissaoVendedorResponse> resultado = service.calcular(vendas);

        assertEquals(2, resultado.size());

        ComissaoVendedorResponse ana = resultado.get(0);
        assertEquals("Ana", ana.getVendedor());
        assertEquals(2, ana.getQuantidadeVendas());
        assertEquals(new BigDecimal("1200.00"), ana.getTotalVendas());
        assertEquals(new BigDecimal("52.00"), ana.getTotalComissao());

        ComissaoVendedorResponse bruno = resultado.get(1);
        assertEquals("Bruno", bruno.getVendedor());
        assertEquals(1, bruno.getQuantidadeVendas());
        assertEquals(new BigDecimal("50.00"), bruno.getTotalVendas());
        assertEquals(new BigDecimal("0.00"), bruno.getTotalComissao());
    }

    @Test
    void calcularTodasUsaAsVendasDoRepositorio() {
        when(vendaRepository.listar()).thenReturn(List.of(
                new Venda("Ana", new BigDecimal("600.00")),
                new Venda("Carlos", new BigDecimal("150.00"))
        ));

        List<ComissaoVendedorResponse> resultado = service.calcularTodas();

        assertEquals(2, resultado.size());
        assertEquals(new BigDecimal("30.00"), resultado.get(0).getTotalComissao());
        assertEquals(new BigDecimal("1.50"), resultado.get(1).getTotalComissao());
    }

    @Test
    void calcularPorVendedorRetornaSomenteOVendedorInformado() {
        when(vendaRepository.listarPorVendedor("Ana")).thenReturn(List.of(
                new Venda("Ana", new BigDecimal("600.00")),
                new Venda("Ana", new BigDecimal("80.00"))
        ));

        ComissaoVendedorResponse resultado = service.calcularPorVendedor("Ana");

        assertEquals("Ana", resultado.getVendedor());
        assertEquals(2, resultado.getQuantidadeVendas());
        assertEquals(new BigDecimal("680.00"), resultado.getTotalVendas());
        assertEquals(new BigDecimal("30.00"), resultado.getTotalComissao());
    }

    @Test
    void calcularPorVendedorInexistenteLancaExcecao() {
        when(vendaRepository.listarPorVendedor("Ninguem")).thenReturn(List.of());

        assertThrows(VendedorNaoEncontradoException.class, () -> service.calcularPorVendedor("Ninguem"));
    }
}
