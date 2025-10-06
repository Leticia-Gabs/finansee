package com.controle.finansee.controller;

import com.controle.finansee.dto.ReceitaDTO;
import com.controle.finansee.service.ReceitaService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receitas")
@CrossOrigin(origins = "*") // Permite que o frontend (de qualquer origem) acesse a API]

public class ReceitaController {

    @Autowired
    private ReceitaService receitaService;

    @PostMapping
    public ResponseEntity<ReceitaDTO> criarReceita(@Valid @RequestBody ReceitaDTO receitaDTO) {
        ReceitaDTO novaReceita = receitaService.criar(receitaDTO);
        return new ResponseEntity<>(novaReceita, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReceitaDTO>> listarTodasReceitas() {
        List<ReceitaDTO> receitas = receitaService.listarTodas();
        return ResponseEntity.ok(receitas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaDTO> buscarReceitaPorId(@PathVariable Long id) {
        ReceitaDTO receita = receitaService.buscarPorId(id);
        return ResponseEntity.ok(receita);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceitaDTO> atualizarReceita(@PathVariable Long id, @Valid @RequestBody ReceitaDTO receitaDTO) {
        ReceitaDTO receitaAtualizada = receitaService.atualizar(id, receitaDTO);
        return ResponseEntity.ok(receitaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarReceita(@PathVariable Long id) {
        receitaService.deletar(id);
        // O padrão de uma API REST é retornar 204 no content
        return ResponseEntity.noContent().build();
    }
}

