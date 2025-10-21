package com.controle.finansee.controller;

import com.controle.finansee.dto.CategoriaDTO;
import com.controle.finansee.model.CategoriaPersonalizada;
import com.controle.finansee.model.user.User;
import com.controle.finansee.service.CategoriaPersonalizadaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaPersonalizadaController {

    @Autowired
    private CategoriaPersonalizadaService service;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listar(@AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(service.listarTodas(usuario));
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> criar(@RequestBody @Valid CategoriaDTO dto, @AuthenticationPrincipal User usuario) {
        CategoriaDTO novaCategoria = service.criarCategoria(dto, usuario);
        return new ResponseEntity<>(novaCategoria, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> atualizar(@PathVariable Long id, @RequestBody @Valid CategoriaDTO dto, @AuthenticationPrincipal User usuario) {
        CategoriaDTO categoriaAtualizada = service.atualizarCategoria(id, dto, usuario);
        return ResponseEntity.ok(categoriaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, @AuthenticationPrincipal User usuario) {
        service.deletarCategoria(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
