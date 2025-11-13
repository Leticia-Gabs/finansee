package com.controle.finansee.service;

import com.controle.finansee.model.CategoriaPersonalizada;
import com.controle.finansee.model.Despesa;
import com.controle.finansee.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class AlertaOrcamentoService {

    @Autowired
    private DespesaRepository despesaRepository;

    // Não precisa mais do OrcamentoRepository!

    public String verificarAlertaOrcamento(Despesa despesa) {
        // 1. Pega o limite diretamente da categoria da despesa
        CategoriaPersonalizada categoria = despesa.getCategoria();
        BigDecimal limite = categoria.getValorLimite();

        // --- LOG 1: VERIFICAR O LIMITE ---
        System.out.println("--- DEBUG ORÇAMENTO ---");
        System.out.println("Categoria: " + categoria.getNome() + " (ID: " + categoria.getId() + ")");
        System.out.println("Limite configurado: " + limite);

        // Se o limite for Nulo ou Zero, não existe orçamento definido. Retorna null.
        if (limite == null || limite.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }

        // 2. Dados para a consulta de soma
        Long usuarioId = despesa.getUsuario().getId();
        Long categoriaId = categoria.getId();
        int ano = despesa.getData().getYear();
        int mes = despesa.getData().getMonthValue();

        // 3. Calcula o total gasto nesta categoria neste mês
        BigDecimal totalGasto = despesaRepository.sumByUsuarioAndCategoriaInMonth(usuarioId, categoriaId, ano, mes);

        if (totalGasto == null) totalGasto = BigDecimal.ZERO;

        // --- LOG 2: VERIFICAR O TOTAL GASTO ---
        System.out.println("Mês/Ano: " + mes + "/" + ano);
        System.out.println("Total Gasto (incluindo atual): " + totalGasto);

        // 4. Calcula a porcentagem
        BigDecimal porcentagem = totalGasto.multiply(BigDecimal.valueOf(100)).divide(limite, 0, RoundingMode.HALF_UP);
        int porc = porcentagem.intValue();

        // --- LOG 3: VERIFICAR A PORCENTAGEM ---
        System.out.println("Porcentagem calculada: " + porc + "%");
        System.out.println("-----------------------");

        // 5. Gera os alertas
        if (porc >= 100) {
            return String.format("ALERTA! Você ultrapassou (%d%%) seu limite de R$%.2f para %s.",
                    porc, limite, categoria.getNome());
        } else if (porc >= 80) {
            return String.format("Atenção! Você atingiu %d%% do limite para %s este mês.",
                    porc, categoria.getNome());
        }

        return null;
    }
}