package com.controle.finansee.service;

import com.controle.finansee.dto.DespesaDTO;
import com.controle.finansee.model.CategoriaPersonalizada;
import com.controle.finansee.model.Despesa;
import com.controle.finansee.model.FormaPagamento;
import com.controle.finansee.model.user.User;
import com.controle.finansee.repository.DespesaRepository;
import com.controle.finansee.repository.CategoriaPersonalizadaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DespesaServiceTest {

    @Mock
    private DespesaRepository despesaRepository;

    @Mock
    private CategoriaPersonalizadaRepository categoriaRepository;

    @InjectMocks
    private DespesaService despesaService;

    private User usuarioA;
    private User usuarioB;
    private CategoriaPersonalizada categoria;
    private Despesa despesaUsuarioA;

    @BeforeEach
    void setUp() {
        // Usuários
        usuarioA = new User();
        usuarioA.setId(1L);
        usuarioA.setName("Andre A");
        usuarioA.setEmail("andreA@test.com");

        usuarioB = new User();
        usuarioB.setId(2L);
        usuarioB.setName("Andre B");
        usuarioB.setEmail("andreB@test.com");

        // Categoria
        categoria = new CategoriaPersonalizada();
        categoria.setId(10L);
        categoria.setNome("Moradia");
        categoria.setCor("#00FF00");
        categoria.setUsuario(usuarioA);

        // Despesa associada ao usuarioA
        despesaUsuarioA = new Despesa();
        despesaUsuarioA.setId(100L);
        despesaUsuarioA.setDescricao("Despesa Teste");
        despesaUsuarioA.setUsuario(usuarioA);
        despesaUsuarioA.setCategoria(categoria);
        despesaUsuarioA.setValor(BigDecimal.valueOf(150));
        despesaUsuarioA.setFormaPagamento(FormaPagamento.PIX);
        despesaUsuarioA.setConta("Conta Teste");
    }

    @Test
    void buscarPorId_deveRetornarDespesa_quandoUsuarioForDono() {
        when(despesaRepository.findById(100L)).thenReturn(Optional.of(despesaUsuarioA));

        DespesaDTO dto = despesaService.buscarPorId(100L, usuarioA);

        assertThat(dto).isNotNull();
        assertThat(dto.categoriaId()).isEqualTo(10L);
        assertThat(dto.valor()).isEqualByComparingTo(BigDecimal.valueOf(150));
    }

    @Test
    void buscarPorId_deveLancarSecurityException_quandoUsuarioNaoForDono() {
        when(despesaRepository.findById(100L)).thenReturn(Optional.of(despesaUsuarioA));

        assertThatThrownBy(() -> despesaService.buscarPorId(100L, usuarioB))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Acesso negado");
    }

    @Test
    void deletar_deveLancarSecurityException_quandoUsuarioNaoForDono() {
        when(despesaRepository.findById(100L)).thenReturn(Optional.of(despesaUsuarioA));

        assertThatThrownBy(() -> despesaService.deletar(100L, usuarioB))
                .isInstanceOf(SecurityException.class);

        verify(despesaRepository, never()).delete(any(Despesa.class));
    }

    @Test
    void buscarPorId_deveLancarEntityNotFound_quandoDespesaNaoExistir() {
        when(despesaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> despesaService.buscarPorId(999L, usuarioA))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Despesa não encontrada");
    }

    @Test
    void deletar_deveRemoverDespesa_quandoUsuarioForDono() {
        when(despesaRepository.findById(100L)).thenReturn(Optional.of(despesaUsuarioA));

        assertDoesNotThrow(() -> despesaService.deletar(100L, usuarioA));

        verify(despesaRepository, times(1)).delete(despesaUsuarioA);
    }
}
