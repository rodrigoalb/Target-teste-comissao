package com.challenge.commission.dto;

import java.math.BigDecimal;

public class Venda {

    private String vendedor;
    private BigDecimal valor;

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
