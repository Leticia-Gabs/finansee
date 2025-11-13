package com.controle.finansee.dto;

import java.math.BigDecimal;
import java.util.List;

// Este DTO agrupa todos os dados necessários para a tela de Dashboard
//
public record DashboardDataDTO(
    BigDecimal saldoAtualGeral,
    BigDecimal totalReceitasGeral,
    BigDecimal totalDespesasGeral,
    List<GastoPorCategoriaDTO> gastosPorCategoria,
    List<ReceitaPorCategoriaDTO> receitasPorCategoria
) {}