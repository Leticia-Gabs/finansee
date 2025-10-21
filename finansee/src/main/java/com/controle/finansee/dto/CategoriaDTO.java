package com.controle.finansee.dto;

import com.controle.finansee.model.TipoCategoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CategoriaDTO (
        Long id,

        @NotBlank(message = "O nome da categoria é obrigatório.")
        String nome,

        @NotNull(message = "O tipo (RECEITA ou DESPESA) é obrigatório.")
        TipoCategoria tipo,

        @NotBlank(message = "A cor é obrigatória.")
        @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "A cor deve estar no formato hexadecimal (ex: #FF5733)")
        String cor
){
}
