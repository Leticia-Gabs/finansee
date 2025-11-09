package com.controle.finansee.controller;

import com.controle.finansee.dto.ReceitaDTO;
import com.controle.finansee.model.user.User;
import com.controle.finansee.service.ReceitaService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receitas")
@CrossOrigin(origins = "*")

public class ReceitaController {

    @Autowired
    private ReceitaService receitaService;

    @PostMapping
    public ResponseEntity<ReceitaDTO> criarReceita(
            @Valid @RequestBody ReceitaDTO receitaDTO,
            @AuthenticationPrincipal User usuario) {
        ReceitaDTO novaReceita = receitaService.criar(receitaDTO, usuario);
        return new ResponseEntity<>(novaReceita, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceitaDTO> atualizarReceita(
            @PathVariable Long id,
            @Valid @RequestBody ReceitaDTO receitaDTO,
            @AuthenticationPrincipal User usuario) {

        ReceitaDTO receitaAtualizada = receitaService.atualizar(id, receitaDTO, usuario);
        return ResponseEntity.ok(receitaAtualizada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaDTO> buscarReceitaPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal User usuario) {

        ReceitaDTO receita = receitaService.buscarPorId(id, usuario);
        return ResponseEntity.ok(receita);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarReceita(
            @PathVariable Long id,
            @AuthenticationPrincipal User usuario) {

        receitaService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}

