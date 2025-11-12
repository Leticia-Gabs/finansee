package com.controle.finansee.controller;

import com.controle.finansee.dto.GastoPorCategoriaDTO;
import com.controle.finansee.dto.ResumoMesDTO;
import com.controle.finansee.model.user.User;
import com.controle.finansee.service.RelatorioService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelatorioControllerTest {

    @Mock
    private RelatorioService relatorioService;

    @InjectMocks
    private RelatorioController relatorioController;

    @Test
    void deveRetornarResumoDoMes() {
        // Arrange
        User usuario = new User();
        int ano = 2025;
        int mes = 10;

        ResumoMesDTO resumo = new ResumoMesDTO(
                new BigDecimal("100.00"),
                new BigDecimal("50.00"),
                new BigDecimal("50.00"),
                List.of()
        );

        when(relatorioService.getResumoDoMes(usuario, ano, mes)).thenReturn(resumo);

        // Act
        ResponseEntity<ResumoMesDTO> response = relatorioController.getResumoMes(usuario, ano, mes);

        // Assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(resumo);

        verify(relatorioService, times(1)).getResumoDoMes(usuario, ano, mes);
    }

    @Test
    void deveExportarPdfComSucesso() throws IOException {
        // Arrange
        User usuario = new User();
        int ano = 2025;
        int mes = 10;

        ByteArrayInputStream mockPdf = new ByteArrayInputStream("pdf".getBytes());
        when(relatorioService.gerarPdfTransacoes(usuario, ano, mes)).thenReturn(mockPdf);

        // Act
        ResponseEntity<?> response = relatorioController.exportarTransacoes(usuario, ano, mes);

        // Assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getHeaders().get("Content-Disposition").get(0))
                .contains("relatorio_2025_10.pdf");

        verify(relatorioService, times(1)).gerarPdfTransacoes(usuario, ano, mes);
    }

    @Test
    void deveRetornarErroAoGerarPdf() throws IOException {
        // Arrange
        User usuario = new User();
        int ano = 2025;
        int mes = 10;

        when(relatorioService.gerarPdfTransacoes(usuario, ano, mes))
                .thenThrow(new IOException("Erro ao gerar PDF"));

        // Act
        ResponseEntity<?> response = relatorioController.exportarTransacoes(usuario, ano, mes);

        // Assert
        assertThat(response.getStatusCode().is5xxServerError()).isTrue();
        verify(relatorioService, times(1)).gerarPdfTransacoes(usuario, ano, mes);
    }
}
