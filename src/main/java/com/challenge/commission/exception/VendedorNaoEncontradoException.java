package com.challenge.commission.exception;

public class VendedorNaoEncontradoException extends RuntimeException {

    public VendedorNaoEncontradoException(String vendedor) {
        super("Vendedor não encontrado: " + vendedor);
    }
}
