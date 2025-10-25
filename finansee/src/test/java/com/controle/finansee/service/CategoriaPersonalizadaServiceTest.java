package com.controle.finansee.service;

import com.controle.finansee.dto.CategoriaDTO;
import com.controle.finansee.model.TipoCategoria;
import com.controle.finansee.model.CategoriaPersonalizada;
import com.controle.finansee.model.user.User;
import com.controle.finansee.repository.CategoriaPersonalizadaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaPersonalizadaServiceTest {

    @Mock
    private CategoriaPersonalizadaRepository repository;

    @InjectMocks
    private CategoriaPersonalizadaService service;

    private User usuarioA;
    private User usuarioB;
    private CategoriaPersonalizada categoriaA;

    @BeforeEach
    void setUp() {
        usuarioA = new User();
        usuarioA.setId(1L);
        usuarioA.setName("Usuario A");
        usuarioA.setEmail("usuarioA@test.com");

        usuarioB = new User();
        usuarioB.setId(2L);
        usuarioB.setName("Usuario B");
        usuarioB.setEmail("usuarioB@test.com");

        categoriaA = new CategoriaPersonalizada();
        categoriaA.setId(100L);
        categoriaA.setNome("Alimentação");
        categoriaA.setTipo(TipoCategoria.DESPESA);
        categoriaA.setUsuario(usuarioA);
    }

    @Test
    void listarTodas_deveRetornarListaDeCategorias() {
        List<CategoriaPersonalizada> lista = new ArrayList<>();
        lista.add(categoriaA);

        when(repository.findAllByUsuarioId(usuarioA.getId())).thenReturn(lista);

        List<CategoriaDTO> result = service.listarTodas(usuarioA);

        assertDoesNotThrow(() -> result);
        verify(repository, times(1)).findAllByUsuarioId(usuarioA.getId());
    }

    @Test
    void criarCategoria_deveSalvarCategoria() {
        CategoriaDTO dto = new CategoriaDTO(null, "Transporte", TipoCategoria.DESPESA, "azul");

        when(repository.existsByNomeAndUsuarioId(dto.nome(), usuarioA.getId())).thenReturn(false);
        when(repository.save(any(CategoriaPersonalizada.class))).thenReturn(categoriaA);

        assertDoesNotThrow(() -> service.criarCategoria(dto, usuarioA));
        verify(repository, times(1)).save(any(CategoriaPersonalizada.class));
    }

    @Test
    void criarCategoria_deveLancarException_quandoNomeDuplicado() {
        CategoriaDTO dto = new CategoriaDTO(null, "Alimentação", TipoCategoria.DESPESA, "vermelho");

        when(repository.existsByNomeAndUsuarioId(dto.nome(), usuarioA.getId())).thenReturn(true);

        assertThatThrownBy(() -> service.criarCategoria(dto, usuarioA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Você já possui uma categoria com este nome.");
    }

    @Test
    void atualizarCategoria_deveAtualizarQuandoUsuarioForDono() {
        CategoriaDTO dto = new CategoriaDTO(null, "Lazer", TipoCategoria.DESPESA, "verde");

        when(repository.findById(categoriaA.getId())).thenReturn(Optional.of(categoriaA));
        when(repository.save(any(CategoriaPersonalizada.class))).thenReturn(categoriaA);

        assertDoesNotThrow(() -> service.atualizarCategoria(categoriaA.getId(), dto, usuarioA));
        verify(repository, times(1)).save(any(CategoriaPersonalizada.class));
    }

    @Test
    void atualizarCategoria_deveLancarSecurityException_quandoUsuarioNaoForDono() {
        CategoriaDTO dto = new CategoriaDTO(null, "Lazer", TipoCategoria.DESPESA, "verde");

        when(repository.findById(categoriaA.getId())).thenReturn(Optional.of(categoriaA));

        assertThatThrownBy(() -> service.atualizarCategoria(categoriaA.getId(), dto, usuarioB))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Acesso negado");
    }

    @Test
    void atualizarCategoria_deveLancarException_quandoCategoriaNaoExistir() {
        CategoriaDTO dto = new CategoriaDTO(null, "Lazer", TipoCategoria.DESPESA, "verde");

        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.atualizarCategoria(999L, dto, usuarioA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Categoria não encontrada");
    }

    @Test
    void deletarCategoria_deveDeletarQuandoUsuarioForDono() {
        when(repository.findById(categoriaA.getId())).thenReturn(Optional.of(categoriaA));

        assertDoesNotThrow(() -> service.deletarCategoria(categoriaA.getId(), usuarioA));
        verify(repository, times(1)).delete(categoriaA);
    }

    @Test
    void deletarCategoria_deveLancarSecurityException_quandoUsuarioNaoForDono() {
        when(repository.findById(categoriaA.getId())).thenReturn(Optional.of(categoriaA));

        assertThatThrownBy(() -> service.deletarCategoria(categoriaA.getId(), usuarioB))
                .isInstanceOf(SecurityException.class);
        verify(repository, never()).delete(any(CategoriaPersonalizada.class));
    }

    @Test
    void deletarCategoria_deveLancarException_quandoCategoriaNaoExistir() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deletarCategoria(999L, usuarioA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Categoria não encontrada");
    }
}
