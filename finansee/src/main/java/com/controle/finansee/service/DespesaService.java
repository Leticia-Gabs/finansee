package com.controle.finansee.service;


import com.controle.finansee.dto.DespesaDTO;
import com.controle.finansee.model.Despesa;

import com.controle.finansee.model.TipoCategoria;
import com.controle.finansee.model.CategoriaPersonalizada;
import com.controle.finansee.model.user.User;
import com.controle.finansee.repository.CategoriaPersonalizadaRepository;
import com.controle.finansee.repository.DespesaRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private CategoriaPersonalizadaRepository categoriaRepository;

    // Metodo para mapear Entidade para DTO
    private DespesaDTO toDTO(Despesa despesa) {
        return new DespesaDTO(
                despesa.getId(),
                despesa.getDescricao(),
                despesa.getValor(),
                despesa.getData(),
                despesa.getCategoria().getId(),
                despesa.getFormaPagamento(),
                despesa.getConta()
        );
    }

    // Metodo para mapear DTO para Entidade
    private Despesa toEntity(DespesaDTO dto, User usuario) {
        CategoriaPersonalizada categoria = validaECarregaCategoria(dto.categoriaId(), usuario, TipoCategoria.DESPESA);
        return new Despesa(
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

        // VALIDAÇÃO DE SEGURANÇA: A categoria pertence ao usuário logado?
        if (!categoria.getUsuario().getId().equals(usuario.getId())) {
            throw new SecurityException("Acesso negado: esta categoria não pertence a você.");
        }

        // VALIDAÇÃO DE LÓGICA: É uma categoria do tipo correto?
        if (categoria.getTipo() != tipoEsperado) {
            throw new IllegalArgumentException("Tipo de categoria inválido. Esperado: " + tipoEsperado + ", mas recebido: " + categoria.getTipo());
        }

        return categoria;
    }

    private Despesa validaEDevoveDespesa(Long id, User usuario) {
        Despesa despesa = despesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada com o id: " + id));

        if (!despesa.getUsuario().getId().equals(usuario.getId())) {
            throw new SecurityException("Acesso negado: esta despesa não pertence a você.");
        }

        return despesa;
    }

    public DespesaDTO criar(DespesaDTO despesaDTO, User usuario) {
        Despesa despesa = toEntity(despesaDTO, usuario);
        despesa = despesaRepository.save(despesa);
        return toDTO(despesa);
    }

    public DespesaDTO atualizar(Long id, DespesaDTO despesaDTO, User usuario) {
        // Primeiro, verifica se a despesa existe
        if (!despesaRepository.existsById(id)) {
            throw new EntityNotFoundException("Despesa não encontrada com o id: " + id);
        }
        Despesa despesa = toEntity(despesaDTO, usuario);
        despesa.setId(id); // Garante que estamos atualizando a entidade correta
        despesa = despesaRepository.save(despesa);
        return toDTO(despesa);
    }

    public DespesaDTO buscarPorId(Long id, User usuario) {
        Despesa despesa = validaEDevoveDespesa(id, usuario);
        return toDTO(despesa);
    }

    public void deletar(Long id, User usuario) {
        Despesa despesa = validaEDevoveDespesa(id, usuario);
        despesaRepository.delete(despesa);
    }
}
