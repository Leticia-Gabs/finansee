package com.controle.finansee.controller;

import com.controle.finansee.dto.TransacaoDTO;
import com.controle.finansee.model.user.User;
import com.controle.finansee.service.TransacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransacaoControllerTest {

    @Mock
    private TransacaoService transacaoService;

    @InjectMocks
    private TransacaoController transacaoController;

    private User usuario;
    private TransacaoDTO receitaDTO;
    private TransacaoDTO despesaDTO;

    @BeforeEach
    void setUp() {
        usuario = new User();
        usuario.setId(1L);
        usuario.setName("Andre");
        usuario.setEmail("andre@test.com");

        // Ajuste o construtor do TransacaoDTO conforme seu record
        receitaDTO = new TransacaoDTO(
                1L,
                "Receita Teste",
                LocalDate.now(),
                "RECEITA",
                "Salário",
                "Conta Corrente",
                "DEPOSITO",
                BigDecimal.valueOf(3000),
                10L,
                "Receita Categoria"
        );

        despesaDTO = new TransacaoDTO(
                2L,
                "Despesa Teste",
                LocalDate.now(),
                "DESPESA",
                "Alimentação",
                "Cartão",
                "CREDITO",
                BigDecimal.valueOf(-100),
                20L,
                "Despesa Categoria"
        );
    }

    @Test
    void listarTransacoes_deveRetornarLista() {
        // Corrigindo o stub para aceitar null no tipo
        when(transacaoService.listarTransacoes(
                eq(usuario),
                nullable(String.class),
                any(),
                any(),
                any(),
                any(),
                any()
        )).thenReturn(List.of(receitaDTO, despesaDTO));

        ResponseEntity<List<TransacaoDTO>> response = transacaoController.listarTransacoes(
                usuario,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(2);

        TransacaoDTO primeira = response.getBody().get(0);
        assertThat(primeira.tipo()).isEqualTo("Receita Teste");
        assertThat(primeira.valor()).isEqualByComparingTo(BigDecimal.valueOf(3000));

        TransacaoDTO segunda = response.getBody().get(1);
        assertThat(segunda.tipo()).isEqualTo("Despesa Teste");
        assertThat(segunda.valor()).isEqualByComparingTo(BigDecimal.valueOf(-100));
    }
}
