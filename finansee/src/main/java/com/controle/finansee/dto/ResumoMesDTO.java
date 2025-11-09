package com.controle.finansee.dto;

import java.math.BigDecimal;
import java.util.List;

// Este DTO vai carregar tudo que a tela de Relatório precisa
public record ResumoMesDTO(
        BigDecimal totalReceitas,
        BigDecimal totalDespesas,
        BigDecimal saldoDoMes,
        List<GastoPorCategoriaDTO> gastosPorCategoria
) {}