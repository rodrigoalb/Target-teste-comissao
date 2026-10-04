package com.challenge.commission.repository;

import com.challenge.commission.dto.Venda;
import com.challenge.commission.dto.VendasRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Armazenamento em memória das vendas.
 *
 * Na inicialização carrega as vendas de {@code src/main/resources/vendas.json};
 * depois disso, novas vendas enviadas via API são adicionadas à mesma lista.
 * Os dados existem apenas enquanto a aplicação estiver rodando.
 */
@Repository
public class VendaRepository {

    private final ObjectMapper objectMapper;
    private final List<Venda> vendas = new CopyOnWriteArrayList<>();

    public VendaRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void carregar() throws IOException {
        VendasRequest arquivo = objectMapper.readValue(
                new ClassPathResource("vendas.json").getInputStream(),
                VendasRequest.class
        );
        if (arquivo.getVendas() != null) {
            vendas.addAll(arquivo.getVendas());
        }
    }

    public List<Venda> listar() {
        return List.copyOf(vendas);
    }

    public List<Venda> listarPorVendedor(String vendedor) {
        return vendas.stream()
                .filter(venda -> venda.getVendedor().equalsIgnoreCase(vendedor))
                .toList();
    }

    public List<Venda> adicionar(List<Venda> novasVendas) {
        List<Venda> adicionadas = List.copyOf(novasVendas);
        vendas.addAll(adicionadas);
        return adicionadas;
    }

    public int quantidade() {
        return vendas.size();
    }
}
