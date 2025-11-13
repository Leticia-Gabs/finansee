package com.controle.finansee.service;
import com.controle.finansee.dto.CategoriaDTO;
import com.controle.finansee.model.TipoCategoria;
import com.controle.finansee.model.user.User;
import com.controle.finansee.model.CategoriaPersonalizada;
import com.controle.finansee.repository.CategoriaPersonalizadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaPersonalizadaService {

    @Autowired
    private CategoriaPersonalizadaRepository repository;

    private CategoriaPersonalizada toEntity(CategoriaDTO dto, User usuario) {
        BigDecimal limite = dto.valorLimite();

        // REGRA: Se for RECEITA, ignora o limite (salva como null)
        if (dto.tipo() == TipoCategoria.RECEITA) {
            limite = null;
        }

        return new CategoriaPersonalizada(
                dto.id(),
                dto.nome(),
                dto.tipo(),
                dto.cor(),
                limite,
                usuario
        );
    }

    private CategoriaDTO toDTO(CategoriaPersonalizada entity) {
        return new CategoriaDTO(
                entity.getId(),
                entity.getNome(),
                entity.getTipo(),
                entity.getCor(),
                entity.getValorLimite());
    }

    public List<CategoriaDTO> listarTodas(User usuario) {

        return repository.findAllByUsuarioId(usuario.getId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CategoriaDTO criarCategoria(CategoriaDTO dto, User usuario) {
        System.out.println("----- Service toEntity -----");
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo ao criar categoria");
        } else {
            System.out.println("Mapeando para entidade com Usuário ID: " + usuario.getId());
        }
        if (repository.existsByNomeAndUsuarioId(dto.nome(), usuario.getId())) {
            throw new IllegalArgumentException("Você já possui uma categoria com este nome.");
        }
        CategoriaPersonalizada categoria = toEntity(dto, usuario);
        categoria = repository.save(categoria);
        return toDTO(categoria);
    }


    // Adicionei o metodo de ATUALIZAR para um CRUD completo
    public CategoriaDTO atualizarCategoria(Long id, CategoriaDTO dto, User usuario) {
        CategoriaPersonalizada categoria = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada."));

        // Garante que o usuário só pode editar sua própria categoria
        if (!categoria.getUsuario().getId().equals(usuario.getId())) {
            throw new SecurityException("Acesso negado.");
        }

        categoria.setNome(dto.nome());
        categoria.setTipo(dto.tipo());
        categoria.setCor(dto.cor());

        categoria = repository.save(categoria);
        return toDTO(categoria);
    }

    public void deletarCategoria(Long id, User usuario) {
        CategoriaPersonalizada categoria = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada."));

        if (!categoria.getUsuario().getId().equals(usuario.getId())) {
            throw new SecurityException("Acesso negado.");
        }
        repository.delete(categoria);
    }
}
