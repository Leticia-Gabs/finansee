package com.controle.finansee.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoDTO(
        Long id,
        String tipo,
        LocalDate data,
        String descricao,
        String categoriaNome,
        String categoriaCor,
        String conta,
        BigDecimal valor,

        // --- ADICIONAR ESTES DOIS CAMPOS ---
        Long categoriaId,
        String formaPagamento // ex: "PIX", "CARTAO_CREDITO"
) {
}
