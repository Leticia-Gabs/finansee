package com.controle.finansee.service;

import com.controle.finansee.dto.ReceitaDTO;
import com.controle.finansee.model.CategoriaPersonalizada;
import com.controle.finansee.model.TipoCategoria;
import com.controle.finansee.model.Receita;
import com.controle.finansee.model.user.User;
import com.controle.finansee.repository.CategoriaPersonalizadaRepository;
import com.controle.finansee.repository.ReceitaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReceitaServiceTest {

    @Mock
    private ReceitaRepository receitaRepository;

    @Mock
    private CategoriaPersonalizadaRepository categoriaRepository;

    @InjectMocks
    private ReceitaService receitaService;

    private User usuarioA;
    private User usuarioB;
    private CategoriaPersonalizada categoriaA;
    private Receita receitaUsuarioA;

    @BeforeEach
    void setUp() {
        usuarioA = new User();
        usuarioA.setId(1L);
        usuarioA.setName("Alan A");
        usuarioA.setEmail("alanA@test.com");

        usuarioB = new User();
        usuarioB.setId(2L);
        usuarioB.setName("Alan B");
        usuarioB.setEmail("alanB@test.com");

        categoriaA = new CategoriaPersonalizada();
        categoriaA.setId(10L);
        categoriaA.setTipo(TipoCategoria.RECEITA);
        categoriaA.setUsuario(usuarioA);

        receitaUsuarioA = new Receita();
        receitaUsuarioA.setId(100L);
        receitaUsuarioA.setDescricao("Receita Teste");
        receitaUsuarioA.setValor(new BigDecimal("500.00"));
        receitaUsuarioA.setData(LocalDate.now());
        receitaUsuarioA.setUsuario(usuarioA);
        receitaUsuarioA.setCategoria(categoriaA);
    }

    @Test
    void criar_deveSalvarReceita() {
        ReceitaDTO dto = new ReceitaDTO(null, "Nova Receita", new BigDecimal("200.00"), LocalDate.now(), categoriaA.getId(), null, null);
        when(categoriaRepository.findById(categoriaA.getId())).thenReturn(Optional.of(categoriaA));
        when(receitaRepository.save(any(Receita.class))).thenReturn(receitaUsuarioA);

        assertDoesNotThrow(() -> receitaService.criar(dto, usuarioA));
        verify(receitaRepository, times(1)).save(any(Receita.class));
    }

    @Test
    void buscarPorId_deveRetornarReceita_quandoUsuarioForDono() {
        when(receitaRepository.findById(100L)).thenReturn(Optional.of(receitaUsuarioA));

        assertDoesNotThrow(() -> receitaService.buscarPorId(100L, usuarioA));
    }

    @Test
    void buscarPorId_deveLancarSecurityException_quandoUsuarioNaoForDono() {
        when(receitaRepository.findById(100L)).thenReturn(Optional.of(receitaUsuarioA));

        assertThatThrownBy(() -> receitaService.buscarPorId(100L, usuarioB))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Acesso negado");
    }

    @Test
    void buscarPorId_deveLancarEntityNotFound_quandoReceitaNaoExistir() {
        when(receitaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> receitaService.buscarPorId(999L, usuarioA))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deletar_deveLancarSecurityException_quandoUsuarioNaoForDono() {
        when(receitaRepository.findById(100L)).thenReturn(Optional.of(receitaUsuarioA));

        assertThatThrownBy(() -> receitaService.deletar(100L, usuarioB))
                .isInstanceOf(SecurityException.class);

        verify(receitaRepository, never()).delete(any(Receita.class));
    }

    @Test
    void deletar_deveChamarDelete_quandoUsuarioForDono() {
        when(receitaRepository.findById(100L)).thenReturn(Optional.of(receitaUsuarioA));

        assertDoesNotThrow(() -> receitaService.deletar(100L, usuarioA));
        verify(receitaRepository, times(1)).delete(receitaUsuarioA);
    }

    @Test
    void atualizar_deveLancarEntityNotFound_quandoReceitaNaoExistir() {
        when(receitaRepository.existsById(999L)).thenReturn(false);

        ReceitaDTO dto = new ReceitaDTO(999L, "Desc", new BigDecimal("100"), LocalDate.now(), categoriaA.getId(), null, null);

        assertThatThrownBy(() -> receitaService.atualizar(999L, dto, usuarioA))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void atualizar_deveSalvarQuandoReceitaExistir() {
        when(receitaRepository.existsById(100L)).thenReturn(true);
        when(categoriaRepository.findById(categoriaA.getId())).thenReturn(Optional.of(categoriaA));
        when(receitaRepository.save(any(Receita.class))).thenReturn(receitaUsuarioA);

        ReceitaDTO dto = new ReceitaDTO(100L, "Desc Atualizada", new BigDecimal("600"), LocalDate.now(), categoriaA.getId(), null, null);

        assertDoesNotThrow(() -> receitaService.atualizar(100L, dto, usuarioA));
        verify(receitaRepository, times(1)).save(any(Receita.class));
    }
}
