package com.controle.finansee.service;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RelatorioServiceTest {

    @Mock
    private DespesaRepository despesaRepository;

    @Mock
    private ReceitaRepository receitaRepository;

    @InjectMocks
    private RelatorioService relatorioService;

    @BeforeEach
    void setUp() {
        // configurações iniciais de mock
    }

    @Test
    void deveGerarArquivoCsvComTransacoesNoPeriodo() {
        // TODO: implementar
    }

    @Test
    void deveGerarArquivoXlsxComTransacoesNoPeriodo() {
        // TODO: implementar
    }

    @Test
    void deveGerarArquivoPdfComTransacoesNoPeriodo() {
        // TODO: implementar
    }

    @Test
    void deveCalcularTotalPorCategoriaNoMesAtual() {
        // TODO: implementar
    }
}
