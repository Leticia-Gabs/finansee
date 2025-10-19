package com.controle.finansee.controller;

import com.controle.finansee.model.CategoriaPersonalizada;
import com.controle.finansee.service.CategoriaPersonalizadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categorias-personalizadas")
public class CategoriaPersonalizadaController {

    @Autowired
    private CategoriaPersonalizadaService service;

    @GetMapping
    public ResponseEntity<List<CategoriaPersonalizada>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @PostMapping
    public ResponseEntity<CategoriaPersonalizada> criar(@RequestBody Map<String, String> body) {
        String nome = body.get("nome");
        return ResponseEntity.ok(service.criarCategoria(nome));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
