package com.challenge.commission.controller;

import com.challenge.commission.dto.ComissaoVendedorResponse;
import com.challenge.commission.dto.VendasRequest;
import com.challenge.commission.service.ComissaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/comissoes")
public class ComissaoController {

    private final ComissaoService comissaoService;
    private final ObjectMapper objectMapper;

    public ComissaoController(ComissaoService comissaoService, ObjectMapper objectMapper) {
        this.comissaoService = comissaoService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<List<ComissaoVendedorResponse>> calcularDoArquivo() throws IOException {
        VendasRequest dados = objectMapper.readValue(
                new ClassPathResource("vendas.json").getInputStream(),
                VendasRequest.class
        );
        return ResponseEntity.ok(comissaoService.calcular(dados.getVendas()));
    }

    @PostMapping
    public ResponseEntity<List<ComissaoVendedorResponse>> calcularEnviado(@RequestBody VendasRequest request) {
        return ResponseEntity.ok(comissaoService.calcular(request.getVendas()));
    }
}
