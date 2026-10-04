package com.challenge.commission.dto;

import java.math.BigDecimal;

public class ComissaoVendaResponse {

    private BigDecimal valorVenda;
    private BigDecimal percentual;
    private BigDecimal comissao;

    public ComissaoVendaResponse(BigDecimal valorVenda, BigDecimal percentual, BigDecimal comissao) {
        this.valorVenda = valorVenda;
        this.percentual = percentual;
        this.comissao = comissao;
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public BigDecimal getPercentual() {
        return percentual;
    }

    public BigDecimal getComissao() {
        return comissao;
    }
}
