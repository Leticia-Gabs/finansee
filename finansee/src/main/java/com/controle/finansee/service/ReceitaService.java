package com.controle.finansee.service;


import com.controle.finansee.dto.ReceitaDTO;
import com.controle.finansee.model.Receita;
import com.controle.finansee.repository.ReceitaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    // Metodo para mapear Entidade para DTO
    private ReceitaDTO toDTO(Receita receita) {
        return new ReceitaDTO(
                receita.getId(),
                receita.getDescricao(),
                receita.getValor(),
                receita.getData(),
                receita.getCategoria()
        );
    }

    // Metodo para mapear DTO para Entidade
    private Receita toEntity(ReceitaDTO dto) {
        return new Receita(
                dto.id(),
                dto.descricao(),
                dto.valor(),
                dto.data(),
                dto.categoria()
        );
    }

    public List<ReceitaDTO> listarTodas() {
        return receitaRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ReceitaDTO buscarPorId(Long id) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Receita não encontrada com o id: " + id));
        return toDTO(receita);
    }

    public ReceitaDTO criar(ReceitaDTO receitaDTO) {
        Receita receita = toEntity(receitaDTO);
        receita = receitaRepository.save(receita);
        return toDTO(receita);
    }

    public ReceitaDTO atualizar(Long id, ReceitaDTO receitaDTO) {
        // Primeiro, verifica se a receita existe
        if (!receitaRepository.existsById(id)) {
            throw new EntityNotFoundException("Receita não encontrada com o id: " + id);
        }
        Receita receita = toEntity(receitaDTO);
        receita.setId(id); // Garante que estamos atualizando a entidade correta
        receita = receitaRepository.save(receita);
        return toDTO(receita);
    }

    public void deletar(Long id) {
        if (!receitaRepository.existsById(id)) {
            throw new EntityNotFoundException("Receita não encontrada com o id: " + id);
        }
        receitaRepository.deleteById(id);
    }
}
