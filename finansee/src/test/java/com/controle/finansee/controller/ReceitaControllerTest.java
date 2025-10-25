package com.controle.finansee.controller;

import com.controle.finansee.dto.ReceitaDTO;
import com.controle.finansee.model.FormaPagamento;
import com.controle.finansee.model.user.User;
import com.controle.finansee.service.ReceitaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceitaControllerTest {

    @Mock
    private ReceitaService receitaService;

    @InjectMocks
    private ReceitaController receitaController;

    private User usuario;
    private ReceitaDTO receitaDTO;

    @BeforeEach
    void setUp() {
        usuario = new User();
        usuario.setId(1L);
        usuario.setName("Andre A");
        usuario.setEmail("andreA@test.com");

        receitaDTO = new ReceitaDTO(
        200L,
        "Salário",
        BigDecimal.valueOf(5000),
        LocalDate.now(),
        1L, // categoriaId
        FormaPagamento.PIX, // Alterado de DEPOSITO
        "Conta 1"
        );
    }

    @Test
    void criarReceita_deveRetornarReceitaCriadaComStatus201() {
        when(receitaService.criar(receitaDTO, usuario)).thenReturn(receitaDTO);

        ResponseEntity<ReceitaDTO> response = receitaController.criarReceita(receitaDTO, usuario);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(receitaDTO);

        verify(receitaService, times(1)).criar(receitaDTO, usuario);
    }

    @Test
    void atualizarReceita_deveRetornarReceitaAtualizadaComStatus200() {
        when(receitaService.atualizar(200L, receitaDTO, usuario)).thenReturn(receitaDTO);

        ResponseEntity<ReceitaDTO> response = receitaController.atualizarReceita(200L, receitaDTO, usuario);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(receitaDTO);

        verify(receitaService, times(1)).atualizar(200L, receitaDTO, usuario);
    }

    @Test
    void buscarReceitaPorId_deveRetornarReceitaComStatus200() {
        when(receitaService.buscarPorId(200L, usuario)).thenReturn(receitaDTO);

        ResponseEntity<ReceitaDTO> response = receitaController.buscarReceitaPorId(200L, usuario);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(receitaDTO);

        verify(receitaService, times(1)).buscarPorId(200L, usuario);
    }

    @Test
    void deletarReceita_deveChamarServiceERetornarNoContent() {
        doNothing().when(receitaService).deletar(200L, usuario);

        ResponseEntity<Void> response = receitaController.deletarReceita(200L, usuario);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(receitaService, times(1)).deletar(200L, usuario);
    }
}
