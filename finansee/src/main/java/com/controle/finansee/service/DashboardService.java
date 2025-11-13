package com.controle.finansee.service;

import com.controle.finansee.dto.DashboardDataDTO;
import com.controle.finansee.dto.GastoPorCategoriaDTO;
import com.controle.finansee.dto.ReceitaPorCategoriaDTO;
import com.controle.finansee.model.user.User;
import com.controle.finansee.repository.DespesaRepository;
import com.controle.finansee.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    public DashboardDataDTO getDashboardData(User usuario) {
        Long usuarioId = usuario.getId();

        // 1. Buscar Totais (para os SummaryCards)
        BigDecimal totalReceitas = receitaRepository.sumTotalByUsuarioId(usuarioId);
        BigDecimal totalDespesas = despesaRepository.sumTotalByUsuarioId(usuarioId);

        // Trata valores nulos (caso não haja transações)
        totalReceitas = (totalReceitas == null) ? BigDecimal.ZERO : totalReceitas;
        totalDespesas = (totalDespesas == null) ? BigDecimal.ZERO : totalDespesas;

        BigDecimal saldoAtual = totalReceitas.subtract(totalDespesas);

        // 2. Buscar Dados Agregados (para os Gráficos)
        List<GastoPorCategoriaDTO> gastos = despesaRepository.findGastosPorCategoriaParaDashboard(usuarioId);
        List<ReceitaPorCategoriaDTO> receitas = receitaRepository.findReceitasPorCategoria(usuarioId);

        // 3. Montar e retornar o DTO
        return new DashboardDataDTO(
                saldoAtual,
                totalReceitas,
                totalDespesas,
                gastos,
                receitas
        );
    }
}