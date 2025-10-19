package com.controle.finansee.service;

import com.controle.finansee.exception.ResourceNotFoundException;
import com.controle.finansee.model.CategoriaPersonalizada;
import com.controle.finansee.repository.CategoriaPersonalizadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaPersonalizadaService {

    @Autowired
    private CategoriaPersonalizadaRepository repository;

    public List<CategoriaPersonalizada> listarTodas() {
        return repository.findAll();
    }

    public CategoriaPersonalizada criarCategoria(String nome) {
        if (repository.existsByNome(nome)) {
            throw new IllegalArgumentException("Já existe uma categoria com esse nome.");
        }
        CategoriaPersonalizada categoria = new CategoriaPersonalizada();
        categoria.setNome(nome);
        return repository.save(categoria);
    }

    public void deletarCategoria(Long id) {
        CategoriaPersonalizada categoria = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada."));
        repository.delete(categoria);
    }
}
