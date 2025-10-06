package com.controle.finansee.service;


import com.controle.finansee.dto.DespesaDTO;
import com.controle.finansee.model.Despesa;
import com.controle.finansee.repository.DespesaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    // Metodo para mapear Entidade para DTO
    private DespesaDTO toDTO(Despesa despesa) {
        return new DespesaDTO(
                despesa.getId(),
                despesa.getDescricao(),
                despesa.getValor(),
                despesa.getData(),
                despesa.getCategoria(),
                despesa.getFormaPagamento()
        );
    }

    // Metodo para mapear DTO para Entidade
    private Despesa toEntity(DespesaDTO dto) {
        return new Despesa(
                dto.id(),
                dto.descricao(),
                dto.valor(),
                dto.data(),
                dto.categoria(),
                dto.formaPagamento()
        );
    }

    public List<DespesaDTO> listarTodas() {
        return despesaRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public DespesaDTO buscarPorId(Long id) {
        Despesa despesa = despesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada com o id: " + id));
        return toDTO(despesa);
    }

    public DespesaDTO criar(DespesaDTO despesaDTO) {
        Despesa despesa = toEntity(despesaDTO);
        despesa = despesaRepository.save(despesa);
        return toDTO(despesa);
    }

    public DespesaDTO atualizar(Long id, DespesaDTO despesaDTO) {
        // Primeiro, verifica se a despesa existe
        if (!despesaRepository.existsById(id)) {
            throw new EntityNotFoundException("Despesa não encontrada com o id: " + id);
        }
        Despesa despesa = toEntity(despesaDTO);
        despesa.setId(id); // Garante que estamos atualizando a entidade correta
        despesa = despesaRepository.save(despesa);
        return toDTO(despesa);
    }

    public void deletar(Long id) {
        if (!despesaRepository.existsById(id)) {
            throw new EntityNotFoundException("Despesa não encontrada com o id: " + id);
        }
        despesaRepository.deleteById(id);
    }
}
