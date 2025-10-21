package com.controle.finansee.service;


import com.controle.finansee.dto.ReceitaDTO;
import com.controle.finansee.model.Receita;

import com.controle.finansee.model.TipoCategoria;
import com.controle.finansee.model.CategoriaPersonalizada;
import com.controle.finansee.model.user.User;
import com.controle.finansee.repository.CategoriaPersonalizadaRepository;
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

    @Autowired
    private CategoriaPersonalizadaRepository categoriaRepository;

    // Metodo para mapear Entidade para DTO
    private ReceitaDTO toDTO(Receita receita) {
        return new ReceitaDTO(
                receita.getId(),
                receita.getDescricao(),
                receita.getValor(),
                receita.getData(),
                receita.getCategoria().getId(),
                receita.getFormaPagamento(),
                receita.getConta()
        );
    }

    // Metodo para mapear DTO para Entidade
    private Receita toEntity(ReceitaDTO dto, User usuario) {
        CategoriaPersonalizada categoria = validaECarregaCategoria(dto.categoriaId(), usuario, TipoCategoria.RECEITA);
        return new Receita(
                dto.id(),
                dto.descricao(),
                dto.valor(),
                dto.data(),
                categoria,
                usuario,
                dto.formaPagamento(),
                dto.conta()
        );
    }

    private CategoriaPersonalizada validaECarregaCategoria(Long categoriaId, User usuario, TipoCategoria tipoEsperado) {
        CategoriaPersonalizada categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com o id: " + categoriaId));

        if (!categoria.getUsuario().getId().equals(usuario.getId())) {
            throw new SecurityException("Acesso negado: esta categoria não pertence a você.");
        }

        if (categoria.getTipo() != tipoEsperado) {
            throw new IllegalArgumentException("Tipo de categoria inválido. Esperado: " + tipoEsperado + ", mas recebido: " + categoria.getTipo());
        }

        return categoria;
    }

    private Receita validaEDevoveReceita(Long id, User usuario) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Receita não encontrada com o id: " + id));

        if (!receita.getUsuario().getId().equals(usuario.getId())) {
            throw new SecurityException("Acesso negado: esta receita não pertence a você.");
        }

        return receita;
    }

    public ReceitaDTO criar(ReceitaDTO receitaDTO, User usuario) {
        Receita receita = toEntity(receitaDTO, usuario);
        receita = receitaRepository.save(receita);
        return toDTO(receita);
    }

    public ReceitaDTO atualizar(Long id, ReceitaDTO receitaDTO, User usuario) {
        if (!receitaRepository.existsById(id)) {
            throw new EntityNotFoundException("Receita não encontrada com o id: " + id);
        }
        Receita receita = toEntity(receitaDTO, usuario);
        receita.setId(id);
        receita = receitaRepository.save(receita);
        return toDTO(receita);
    }

    public ReceitaDTO buscarPorId(Long id, User usuario) {
        Receita receita = validaEDevoveReceita(id, usuario);
        return toDTO(receita);
    }

    public void deletar(Long id, User usuario) {
        Receita receita = validaEDevoveReceita(id, usuario);
        receitaRepository.delete(receita);
    }
}
