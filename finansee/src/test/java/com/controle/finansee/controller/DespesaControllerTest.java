package com.controle.finansee.controller;

import com.controle.finansee.dto.DespesaDTO;
import com.controle.finansee.model.FormaPagamento;
import com.controle.finansee.model.user.User;
import com.controle.finansee.service.DespesaService;
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
class DespesaControllerTest {

    @Mock
    private DespesaService despesaService;

    @InjectMocks
    private DespesaController despesaController;

    private User usuario;
    private DespesaDTO despesaDTO;

    @BeforeEach
    void setUp() {
        usuario = new User();
        usuario.setId(1L);
        usuario.setName("Andre A");
        usuario.setEmail("andreA@test.com");

        despesaDTO = new DespesaDTO(
                100L,
                "Conta de luz",
                BigDecimal.valueOf(150),
                LocalDate.now(),
                1L, // categoriaId
                FormaPagamento.CARTAO_CREDITO,
                "Conta 1"
        );
    }

    @Test
    void criarDespesa_deveRetornarDespesaCriadaComStatus201() {
        when(despesaService.criar(despesaDTO, usuario)).thenReturn(despesaDTO);

        ResponseEntity<DespesaDTO> response = despesaController.criarDespesa(despesaDTO, usuario);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(despesaDTO);

        verify(despesaService, times(1)).criar(despesaDTO, usuario);
    }

    @Test
    void atualizarDespesa_deveRetornarDespesaAtualizadaComStatus200() {
        when(despesaService.atualizar(100L, despesaDTO, usuario)).thenReturn(despesaDTO);

        ResponseEntity<DespesaDTO> response = despesaController.atualizarDespesa(100L, despesaDTO, usuario);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(despesaDTO);

        verify(despesaService, times(1)).atualizar(100L, despesaDTO, usuario);
    }

    @Test
    void buscarDespesaPorId_deveRetornarDespesaComStatus200() {
        when(despesaService.buscarPorId(100L, usuario)).thenReturn(despesaDTO);

        ResponseEntity<DespesaDTO> response = despesaController.buscarDespesaPorId(100L, usuario);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(despesaDTO);

        verify(despesaService, times(1)).buscarPorId(100L, usuario);
    }

    @Test
    void deletarDespesa_deveChamarServiceERetornarNoContent() {
        doNothing().when(despesaService).deletar(100L, usuario);

        ResponseEntity<Void> response = despesaController.deletarDespesa(100L, usuario);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(despesaService, times(1)).deletar(100L, usuario);
    }
}
