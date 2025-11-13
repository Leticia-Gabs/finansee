package com.controle.finansee.dto;
import java.math.BigDecimal;
// DTO separado para receitas, caso a lógica mude no futuro
public record ReceitaPorCategoriaDTO(
        String categoriaNome,
        String categoriaCor,
        BigDecimal totalRecebido
) {}