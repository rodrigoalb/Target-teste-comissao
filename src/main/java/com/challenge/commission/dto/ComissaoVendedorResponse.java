package com.challenge.commission.dto;

import java.math.BigDecimal;
import java.util.List;

public class ComissaoVendedorResponse {

    private String vendedor;
    private int quantidadeVendas;
    private BigDecimal totalVendas;
    private BigDecimal totalComissao;
    private List<ComissaoVendaResponse> detalhes;

    public ComissaoVendedorResponse(String vendedor, int quantidadeVendas, BigDecimal totalVendas,
                                    BigDecimal totalComissao, List<ComissaoVendaResponse> detalhes) {
        this.vendedor = vendedor;
        this.quantidadeVendas = quantidadeVendas;
        this.totalVendas = totalVendas;
        this.totalComissao = totalComissao;
        this.detalhes = detalhes;
    }

    public String getVendedor() {
        return vendedor;
    }

    public int getQuantidadeVendas() {
        return quantidadeVendas;
    }

    public BigDecimal getTotalVendas() {
        return totalVendas;
    }

    public BigDecimal getTotalComissao() {
        return totalComissao;
    }

    public List<ComissaoVendaResponse> getDetalhes() {
        return detalhes;
    }
}
