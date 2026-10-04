package com.challenge.commission.service;

import com.challenge.commission.dto.ComissaoVendaResponse;
import com.challenge.commission.dto.ComissaoVendedorResponse;
import com.challenge.commission.dto.Venda;
import com.challenge.commission.exception.VendedorNaoEncontradoException;
import com.challenge.commission.repository.VendaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComissaoService {

    private static final BigDecimal LIMITE_SEM_COMISSAO = new BigDecimal("100.00");
    private static final BigDecimal LIMITE_COMISSAO_BAIXA = new BigDecimal("500.00");
    private static final BigDecimal PERCENTUAL_BAIXO = new BigDecimal("0.01");
    private static final BigDecimal PERCENTUAL_ALTO = new BigDecimal("0.05");

    private final VendaRepository vendaRepository;

    public ComissaoService(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    /**
     * Calcula a comissão de todos os vendedores considerando todas as vendas
     * registradas em memória (arquivo inicial + vendas adicionadas via API).
     */
    public List<ComissaoVendedorResponse> calcularTodas() {
        return calcular(vendaRepository.listar());
    }

    /**
     * Calcula a comissão de um único vendedor a partir das vendas em memória.
     */
    public ComissaoVendedorResponse calcularPorVendedor(String vendedor) {
        List<Venda> vendas = vendaRepository.listarPorVendedor(vendedor);
        if (vendas.isEmpty()) {
            throw new VendedorNaoEncontradoException(vendedor);
        }
        return montarResumo(vendas.get(0).getVendedor(), vendas);
    }

    /**
     * Calcula a comissão sobre a lista informada, sem consultar nem alterar o armazenamento.
     */
    public List<ComissaoVendedorResponse> calcular(List<Venda> vendas) {
        Map<String, List<Venda>> porVendedor = new LinkedHashMap<>();
        for (Venda venda : vendas) {
            porVendedor.computeIfAbsent(venda.getVendedor(), key -> new ArrayList<>()).add(venda);
        }

        List<ComissaoVendedorResponse> resultado = new ArrayList<>();
        for (Map.Entry<String, List<Venda>> entrada : porVendedor.entrySet()) {
            resultado.add(montarResumo(entrada.getKey(), entrada.getValue()));
        }
        return resultado;
    }

    public BigDecimal calcularComissao(BigDecimal valorVenda) {
        return valorVenda.multiply(percentual(valorVenda)).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal percentual(BigDecimal valorVenda) {
        if (valorVenda.compareTo(LIMITE_SEM_COMISSAO) < 0) {
            return BigDecimal.ZERO;
        }
        if (valorVenda.compareTo(LIMITE_COMISSAO_BAIXA) < 0) {
            return PERCENTUAL_BAIXO;
        }
        return PERCENTUAL_ALTO;
    }

    private ComissaoVendedorResponse montarResumo(String vendedor, List<Venda> vendas) {
        List<ComissaoVendaResponse> detalhes = new ArrayList<>();
        BigDecimal totalVendas = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalComissao = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        for (Venda venda : vendas) {
            BigDecimal valor = venda.getValor().setScale(2, RoundingMode.HALF_UP);
            BigDecimal percentual = percentual(valor);
            BigDecimal comissao = calcularComissao(valor);
            detalhes.add(new ComissaoVendaResponse(valor, percentual, comissao));
            totalVendas = totalVendas.add(valor);
            totalComissao = totalComissao.add(comissao);
        }

        return new ComissaoVendedorResponse(vendedor, vendas.size(), totalVendas, totalComissao, detalhes);
    }
}
