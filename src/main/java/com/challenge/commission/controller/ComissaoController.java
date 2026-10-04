package com.challenge.commission.controller;

import com.challenge.commission.dto.ComissaoVendedorResponse;
import com.challenge.commission.dto.VendasRequest;
import com.challenge.commission.service.ComissaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comissoes")
public class ComissaoController {

    private final ComissaoService comissaoService;

    public ComissaoController(ComissaoService comissaoService) {
        this.comissaoService = comissaoService;
    }

    /**
     * Comissão de todos os vendedores, calculada sobre todas as vendas em memória
     * (as do arquivo vendas.json mais as adicionadas via POST /api/vendas).
     */
    @GetMapping
    public ResponseEntity<List<ComissaoVendedorResponse>> calcularTodas() {
        return ResponseEntity.ok(comissaoService.calcularTodas());
    }

    /**
     * Comissão de um vendedor específico (busca sem diferenciar maiúsculas/minúsculas).
     */
    @GetMapping("/{vendedor}")
    public ResponseEntity<ComissaoVendedorResponse> calcularPorVendedor(@PathVariable String vendedor) {
        return ResponseEntity.ok(comissaoService.calcularPorVendedor(vendedor));
    }

    /**
     * Simulação: calcula a comissão apenas sobre as vendas enviadas no corpo.
     * Nada é salvo em memória.
     */
    @PostMapping
    public ResponseEntity<List<ComissaoVendedorResponse>> simular(@Valid @RequestBody VendasRequest request) {
        return ResponseEntity.ok(comissaoService.calcular(request.getVendas()));
    }
}
