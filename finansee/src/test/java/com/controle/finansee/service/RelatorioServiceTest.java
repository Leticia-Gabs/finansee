package com.controle.finansee.service;

import com.controle.finansee.dto.GastoPorCategoriaDTO;
import com.controle.finansee.dto.ResumoMesDTO;
import com.controle.finansee.dto.TransacaoDTO;
import com.controle.finansee.model.user.User;
import com.controle.finansee.repository.DespesaRepository;
import com.controle.finansee.repository.ReceitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RelatorioServiceTest {

    @Mock
    private DespesaRepository despesaRepository;

    @Mock
    private ReceitaRepository receitaRepository;

    @Mock
    private TransacaoService transacaoService;

    @InjectMocks
    private RelatorioService relatorioService;

    private User usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new User();
        usuario.setId(1L);
    }

    @Test
    void deveGerarResumoDoMesCorretamente() {
        int ano = 2025;
        int mes = 10;

        // Mock do retorno de gastos por categoria
        List<GastoPorCategoriaDTO> gastos = List.of(
                new GastoPorCategoriaDTO("Alimentação", "#FF0000", new BigDecimal("300.00")),
                new GastoPorCategoriaDTO("Transporte", "#00FF00", new BigDecimal("200.00"))
        );
        when(despesaRepository.findGastosPorCategoria(usuario.getId(), ano, mes))
                .thenReturn(gastos);

        // Mock do total de receitas
        when(receitaRepository.sumByUsuarioAndMes(usuario.getId(), ano, mes))
                .thenReturn(new BigDecimal("1000.00"));

        // Executa o método
        ResumoMesDTO resumo = relatorioService.getResumoDoMes(usuario, ano, mes);

        // Verificações
        assertEquals(new BigDecimal("1000.00"), resumo.totalReceitas());
        assertEquals(new BigDecimal("500.00"), resumo.totalDespesas());
        assertEquals(new BigDecimal("500.00"), resumo.saldoDoMes());
        assertEquals(2, resumo.gastosPorCategoria().size());

        verify(despesaRepository).findGastosPorCategoria(usuario.getId(), ano, mes);
        verify(receitaRepository).sumByUsuarioAndMes(usuario.getId(), ano, mes);
    }

    @Test
    void deveGerarPdfTransacoesSemErros() throws IOException {
        int ano = 2025;
        int mes = 10;

        // Mock das transações
        List<TransacaoDTO> transacoes = List.of(
                new TransacaoDTO(
                        1L, "RECEITA", LocalDate.of(2025, 10, 1),
                        "Salário", "Trabalho", "#00FF00",
                        "Nubank", new BigDecimal("3000.00"),
                        1L, "PIX"
                ),
                new TransacaoDTO(
                        2L, "DESPESA", LocalDate.of(2025, 10, 5),
                        "Supermercado", "Alimentação", "#FF0000",
                        "Itaú", new BigDecimal("-500.00"),
                        2L, "Crédito"
                )
        );
        when(transacaoService.listarTransacoes(eq(usuario), any(), any(), any(), any(), any(), any()))
                .thenReturn(transacoes);

        // Executa o método
        ByteArrayInputStream pdf = relatorioService.gerarPdfTransacoes(usuario, ano, mes);

        assertNotNull(pdf);
        assertTrue(pdf.available() > 0);

        verify(transacaoService).listarTransacoes(eq(usuario), any(), any(), any(), any(), any(), any());
    }
}
