package com.controle.finansee.controller;

import com.controle.finansee.service.RelatorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RelatorioController.class)
class RelatorioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RelatorioService relatorioService;

    @BeforeEach
    void setup() {
        // configurações iniciais de mock
    }

    @Test
    void deveRetornarArquivoCsvAoExportar() {
        // TODO: implementar
    }

    @Test
    void deveRetornarErroQuandoFormatoInvalido() {
        // TODO: implementar
    }

    @Test
    void deveRetornarGraficoDeGastosPorCategoria() {
        // TODO: implementar
    }
}
