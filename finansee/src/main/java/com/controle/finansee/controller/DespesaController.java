package com.controle.finansee.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.controle.finansee.model.user.User;
import com.controle.finansee.dto.DespesaDTO;
import com.controle.finansee.service.DespesaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/despesas")
@CrossOrigin(origins = "*") // Permite que o frontend (de qualquer origem) acesse a API
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @PostMapping
    public ResponseEntity<DespesaDTO> criarDespesa(
            @Valid @RequestBody DespesaDTO despesaDTO,
            @AuthenticationPrincipal User usuario) {
        DespesaDTO novaDespesa = despesaService.criar(despesaDTO, usuario);
        return new ResponseEntity<>(novaDespesa, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DespesaDTO> atualizarDespesa(
            @PathVariable Long id,
            @Valid @RequestBody DespesaDTO despesaDTO,
            @AuthenticationPrincipal User usuario) {

        DespesaDTO despesaAtualizada = despesaService.atualizar(id, despesaDTO, usuario);
        return ResponseEntity.ok(despesaAtualizada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaDTO> buscarDespesaPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal User usuario) {

        DespesaDTO despesa = despesaService.buscarPorId(id, usuario);
        return ResponseEntity.ok(despesa);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDespesa(
            @PathVariable Long id,
            @AuthenticationPrincipal User usuario) {

        despesaService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}

