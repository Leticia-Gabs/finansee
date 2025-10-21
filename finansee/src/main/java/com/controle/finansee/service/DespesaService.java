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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

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

    public List<DespesaDTO> filtrar(String categoria, LocalDate dataInicio, LocalDate dataFim,
                                    BigDecimal valorMin, BigDecimal valorMax, User usuario) {
        // Chama o repositório para buscar as despesas filtradas pelos parâmetros informados
        // O repositório deve implementar a lógica para aplicar esses filtros no banco
        List<Despesa> despesas = despesaRepository.filtrarDespesas(categoria, dataInicio, dataFim, valorMin, valorMax, usuario);
        
        // Converte a lista de entidades 'Despesa' para uma lista de DTOs 'DespesaDTO'
        // Isso é útil para não expor diretamente a entidade e facilitar o envio dos dados ao frontend
        return despesas.stream().map(DespesaDTO::new).toList();            
        // Mapeia cada Despesa para um DespesaDTO (presumindo que exista esse construtor) // Coleta o resultado em uma lista
    }

    public Page<DespesaDTO> filtrarComPaginacao(String categoria, LocalDate dataInicio, LocalDate dataFim,
                                           BigDecimal valorMin, BigDecimal valorMax, User usuario, Pageable pageable) {
    // lógica para buscar com filtros e paginação
    }


    public void deletar(Long id, User usuario) {
        Despesa despesa = validaEDevoveDespesa(id, usuario);
        despesaRepository.delete(despesa);
    }




    public Page<DespesaDTO> filtrarComPaginacao(String categoria, LocalDate dataInicio, LocalDate dataFim,
                                            BigDecimal valorMin, BigDecimal valorMax, User usuario, Pageable pageable) {

        Specification<Despesa> spec = Specification.where(DespesaSpecifications.pertenceAoUsuario(usuario.getId()));

        if (categoria != null && !categoria.isEmpty()) {
            spec = spec.and(DespesaSpecifications.comCategoria(categoria));
        }
        if (dataInicio != null) {
            spec = spec.and(DespesaSpecifications.dataMaiorOuIgual(dataInicio));
        }
        if (dataFim != null) {
            spec = spec.and(DespesaSpecifications.dataMenorOuIgual(dataFim));
        }
        if (valorMin != null) {
            spec = spec.and(DespesaSpecifications.valorMaiorOuIgual(valorMin));
        }
        if (valorMax != null) {
            spec = spec.and(DespesaSpecifications.valorMenorOuIgual(valorMax));
        }

        Page<Despesa> despesasPage = despesaRepository.findAll(spec, pageable);

        return despesasPage.map(DespesaDTO::new);
    }

}
