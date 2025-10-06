package com.controle.finansee.dto;

import com.controle.finansee.model.Categoria;
import com.controle.finansee.model.FormaPagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DespesaDTO(
        Long id,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser positivo")
        BigDecimal valor,

        @NotNull(message = "Data é obrigatória")
        LocalDate data,

        @NotNull(message = "Categoria é obrigatória")
        Categoria categoria,

        @NotNull(message = "Forma de pagamento é obrigatória")
        FormaPagamento formaPagamento
) {
}
