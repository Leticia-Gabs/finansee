package com.controle.finansee.dto;

import java.math.BigDecimal;

public record GastoPorCategoriaDTO(
        String categoriaNome,
        String categoriaCor,
        BigDecimal totalGasto
) {}
