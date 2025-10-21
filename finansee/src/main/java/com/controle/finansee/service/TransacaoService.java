package com.controle.finansee.service;

import com.controle.finansee.dto.TransacaoDTO;
import com.controle.finansee.model.user.User;
import com.controle.finansee.repository.DespesaRepository;
import com.controle.finansee.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class TransacaoService {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private ReceitaRepository receitaRepository;

    public List<TransacaoDTO> listarTransacoes(User usuario) {

        // 1. Buscar todas as despesas do usuário
        List<TransacaoDTO> despesas = despesaRepository.findAllByUsuarioId(usuario.getId())
                .stream()
                .map(d -> new TransacaoDTO(
                        d.getId(),
                        "DESPESA",
                        d.getData(),
                        d.getDescricao(),
                        d.getCategoria().getNome(), // Resolve o nome da categoria
                        d.getCategoria().getCor(),  // Resolve a cor
                        d.getConta(),
                        d.getValor().negate() // <-- Negativa o valor
                ))
                .toList();

        // 2. Buscar todas as receitas do usuário
        List<TransacaoDTO> receitas = receitaRepository.findAllByUsuarioId(usuario.getId())
                .stream()
                .map(r -> new TransacaoDTO(
                        r.getId(),
                        "RECEITA",
                        r.getData(),
                        r.getDescricao(),
                        r.getCategoria().getNome(), // Resolve o nome da categoria
                        r.getCategoria().getCor(),  // Resolve a cor
                        r.getConta(),
                        r.getValor() // O valor já é positivo
                ))
                .toList();

        // 3. Juntar as duas listas, ordenar por data (mais recente primeiro) e retornar
        return Stream.concat(despesas.stream(), receitas.stream())
                .sorted(Comparator.comparing(TransacaoDTO::data).reversed())
                .toList();
    }
}