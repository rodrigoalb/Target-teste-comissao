package com.challenge.commission.controller;

import com.challenge.commission.dto.Venda;
import com.challenge.commission.dto.VendasRequest;
import com.challenge.commission.repository.VendaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    private final VendaRepository vendaRepository;

    public VendaController(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    /**
     * Lista todas as vendas registradas em memória.
     */
    @GetMapping
    public ResponseEntity<List<Venda>> listar() {
        return ResponseEntity.ok(vendaRepository.listar());
    }

    /**
     * Adiciona vendas ao armazenamento em memória e devolve apenas as vendas adicionadas.
     * A partir daí elas passam a entrar no cálculo de GET /api/comissoes.
     */
    @PostMapping
    public ResponseEntity<List<Venda>> adicionar(@Valid @RequestBody VendasRequest request) {
        List<Venda> adicionadas = vendaRepository.adicionar(request.getVendas());
        return ResponseEntity.status(HttpStatus.CREATED).body(adicionadas);
    }
}
