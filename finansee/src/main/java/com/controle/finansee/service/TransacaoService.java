package com.controle.finansee.service;

import com.controle.finansee.dto.TransacaoDTO;
import com.controle.finansee.model.Despesa;
import com.controle.finansee.model.Receita;
import com.controle.finansee.model.user.User;
import com.controle.finansee.repository.DespesaRepository;
import com.controle.finansee.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import com.controle.finansee.specification.TransacaoEspecification; // Importe
import org.springframework.data.jpa.domain.Specification; // Importe
import org.springframework.data.domain.Sort; // Importe Sort
@Service
public class TransacaoService {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private ReceitaRepository receitaRepository;

    public List<TransacaoDTO> listarTransacoes(User usuario,
                                               String tipo,
                                               Long categoriaId,
                                               LocalDate dataInicio,
                                               LocalDate dataFim,
                                               BigDecimal valorMin,
                                               BigDecimal valorMax) {


        // 1. Cria a Specification com os filtros
        Specification<Despesa> despesaSpec = TransacaoEspecification.RASCUNHO( // Refatore o nome RASCUNHO depois
                usuario, categoriaId, dataInicio, dataFim, valorMin, valorMax
        );
        Specification<Receita> receitaSpec = TransacaoEspecification.RASCUNHO(
                usuario, categoriaId, dataInicio, dataFim, valorMin, valorMax
        );

        // Define a ordenação (mais recente primeiro)
        Sort sort = Sort.by(Sort.Direction.DESC, "data", "id");

        List<TransacaoDTO> despesasDTO = new ArrayList<>(); // Inicia vazia
        List<TransacaoDTO> receitasDTO = new ArrayList<>(); // Inicia vazia

        boolean buscarDespesas = (tipo == null || tipo.isEmpty() || tipo.equalsIgnoreCase("DESPESA"));
        boolean buscarReceitas = (tipo == null || tipo.isEmpty() || tipo.equalsIgnoreCase("RECEITA"));

        // 1. Buscar todas as despesas do usuário
        if (buscarDespesas) {
            despesasDTO = despesaRepository.findAll(despesaSpec, sort)
                    .stream()
                    .map(d -> new TransacaoDTO(
                            d.getId(),
                            "DESPESA",
                            d.getData(),
                            d.getDescricao(),
                            d.getCategoria().getNome(), // Resolve o nome da categoria
                            d.getCategoria().getCor(),  // Resolve a cor
                            d.getConta(),
                            d.getValor().negate(), // <-- Negativa o valor
                            d.getCategoria().getId(),
                            d.getFormaPagamento().name() // Converte o Enum para String
                    ))
                    .toList();
        }
        // 2. Buscar todas as receitas do usuário
        if (buscarReceitas) {
             receitasDTO = receitaRepository.findAll(receitaSpec, sort)
                    .stream()
                    .map(r -> new TransacaoDTO(
                            r.getId(),
                            "RECEITA",
                            r.getData(),
                            r.getDescricao(),
                            r.getCategoria().getNome(), // Resolve o nome da categoria
                            r.getCategoria().getCor(),  // Resolve a cor
                            r.getConta(),
                            r.getValor(), // O valor já é positivo
                            r.getCategoria().getId(),
                            r.getFormaPagamento().name() // Converte o Enum para String
                    ))
                    .toList();
        }
        // 3. Juntar as duas listas, ordenar por data (mais recente primeiro) e retornar
        System.out.println("Listando transações!");
        return Stream.concat(despesasDTO.stream(), receitasDTO.stream())
                .sorted(Comparator.comparing(TransacaoDTO::data).reversed()
                        .thenComparing(TransacaoDTO::id).reversed())
                .toList();
    }
}