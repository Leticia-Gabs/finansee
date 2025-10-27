package com.controle.finansee.controller;

import com.controle.finansee.dto.CategoriaDTO;
import com.controle.finansee.model.TipoCategoria;
import com.controle.finansee.model.user.User;
import com.controle.finansee.service.CategoriaPersonalizadaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoriaPersonalizadaControllerTest {

    @Mock
    private CategoriaPersonalizadaService service;

    @InjectMocks
    private CategoriaPersonalizadaController controller;

    private User usuario;
    private CategoriaDTO categoriaDTO;

    @BeforeEach
    void setUp() {
        usuario = new User(1L, "Andre", "andre@test.com", "123456"); // Exemplo de construtor
        categoriaDTO = new CategoriaDTO(10L, "Categoria Teste", TipoCategoria.DESPESA, "Descrição teste");
    }

    @Test
    void listar_deveRetornarLista() {
        when(service.listarTodas(usuario)).thenReturn(List.of(categoriaDTO));

        ResponseEntity<List<CategoriaDTO>> response = controller.listar(usuario);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).nome()).isEqualTo("Categoria Teste"); // getter do record
    }

    @Test
    void criar_deveRetornarCategoriaCriada() {
        when(service.criarCategoria(eq(categoriaDTO), eq(usuario))).thenReturn(categoriaDTO);

        ResponseEntity<CategoriaDTO> response = controller.criar(categoriaDTO, usuario);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody().nome()).isEqualTo("Categoria Teste");
    }

    @Test
    void atualizar_deveRetornarCategoriaAtualizada() {
        when(service.atualizarCategoria(eq(10L), eq(categoriaDTO), eq(usuario))).thenReturn(categoriaDTO);

        ResponseEntity<CategoriaDTO> response = controller.atualizar(10L, categoriaDTO, usuario);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().nome()).isEqualTo("Categoria Teste");
    }

    @Test
    void deletar_deveRetornarNoContent() {
        ResponseEntity<Void> response = controller.deletar(10L, usuario);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(204);
    }
}
