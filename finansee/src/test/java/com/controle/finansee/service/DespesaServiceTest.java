package com.controle.finansee.service;

import com.controle.finansee.model.Despesa;
import com.controle.finansee.model.user.User;
import com.controle.finansee.repository.DespesaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DespesaServiceTest {

    @Mock
    private DespesaRepository despesaRepository;

    @InjectMocks
    private DespesaService despesaService;

    private User usuarioA;
    private User usuarioB;
    private Despesa despesaUsuarioA;

    @BeforeEach
    void setUp() {
        usuarioA = new User();
        usuarioA.setId(1L);
        usuarioA.setName("Andre A");
        usuarioA.setEmail("andreA@test.com");

        usuarioB = new User();
        usuarioB.setId(2L);
        usuarioB.setName("Andre B");
        usuarioB.setEmail("andreB@test.com");

        despesaUsuarioA = new Despesa();
        despesaUsuarioA.setId(100L);
        despesaUsuarioA.setDescricao("Despesa Teste");
        despesaUsuarioA.setUsuario(usuarioA);
    }

    @Test
    void buscarPorId_deveRetornarDespesa_quandoUsuarioForDono() {
        when(despesaRepository.findById(100L)).thenReturn(Optional.of(despesaUsuarioA));
        assertDoesNotThrow(() -> {
            despesaService.buscarPorId(100L, usuarioA);
        });
    }

    @Test
    void buscarPorId_deveLancarSecurityException_quandoUsuarioNaoForDono() {
        when(despesaRepository.findById(100L)).thenReturn(Optional.of(despesaUsuarioA));

        assertThatThrownBy(() -> {
            despesaService.buscarPorId(100L, usuarioB);
        }).isInstanceOf(SecurityException.class).hasMessageContaining("Acesso negado");
    }

    @Test
    void deletar_deveLancarSecurityException_quandoUsuarioNaoForDono() {
        when(despesaRepository.findById(100L)).thenReturn(Optional.of(despesaUsuarioA));
        assertThatThrownBy(() -> {
            despesaService.deletar(100L, usuarioB);
        }).isInstanceOf(SecurityException.class);

        verify(despesaRepository, never()).delete(any(Despesa.class));
    }

    @Test
    void buscarPorId_deveLancarEntityNotFound_quandoDespesaNaoExistir() {
        when(despesaRepository.findById(999L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> {
            despesaService.buscarPorId(999L, usuarioA);
        }).isInstanceOf(EntityNotFoundException.class);
    }
}
