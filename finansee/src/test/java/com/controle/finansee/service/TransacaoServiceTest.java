package com.controle.finansee.service;

import com.controle.finansee.dto.TransacaoDTO;
import com.controle.finansee.model.*;
import com.controle.finansee.model.user.User;
import com.controle.finansee.repository.DespesaRepository;
import com.controle.finansee.repository.ReceitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    @Mock
    private DespesaRepository despesaRepository;

    @Mock
    private ReceitaRepository receitaRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    private User usuario;
    private CategoriaPersonalizada categoria;

    @BeforeEach
    void setup() {
        usuario = new User();
        usuario.setId(1L);

        categoria = new CategoriaPersonalizada();
        categoria.setId(10L);
        categoria.setNome("Salario");
        categoria.setCor("#00FF00");
    }

    @Test
    void listarTransacoes_deveRetornarReceitaEDespesa() {
        // Mock da despesa
        Despesa despesa = new Despesa();
        despesa.setId(1L);
        despesa.setDescricao("Conta de luz");
        despesa.setData(LocalDate.now().minusDays(1));
        despesa.setValor(BigDecimal.valueOf(100));
        despesa.setCategoria(categoria);
        despesa.setUsuario(usuario);
        despesa.setFormaPagamento(FormaPagamento.PIX);
        despesa.setConta("Conta 1");

        // Mock da receita
        Receita receita = new Receita();
        receita.setId(2L);
        receita.setDescricao("Salario");
        receita.setData(LocalDate.now());
        receita.setValor(BigDecimal.valueOf(3000));
        receita.setCategoria(categoria);
        receita.setUsuario(usuario);
        receita.setFormaPagamento(FormaPagamento.CARTAO_CREDITO);
        receita.setConta("Conta 1");

        // Configura o comportamento dos mocks
        when(despesaRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(List.of(despesa));
        when(receitaRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(List.of(receita));

        // Executa o service
        List<TransacaoDTO> transacoes = transacaoService.listarTransacoes(
                usuario,
                null, // null = retorna tudo
                null,
                null,
                null,
                null,
                null
        );

    // Verifica que existem 2 transações
    assertThat(transacoes).hasSize(2);

    // Verifica que há uma receita com valor 3000
    assertThat(transacoes)
            .anyMatch(t -> t.tipo().equals("RECEITA") && t.valor().compareTo(BigDecimal.valueOf(3000)) == 0);

    // Verifica que há uma despesa com valor -100
    assertThat(transacoes)
            .anyMatch(t -> t.tipo().equals("DESPESA") && t.valor().compareTo(BigDecimal.valueOf(-100)) == 0);

    }

    @Test
    void listarTransacoes_deveFiltrarApenasDespesas() {
        Despesa despesa = new Despesa();
        despesa.setId(1L);
        despesa.setDescricao("Conta de luz");
        despesa.setData(LocalDate.now());
        despesa.setValor(BigDecimal.valueOf(50));
        despesa.setCategoria(categoria);
        despesa.setUsuario(usuario);
        despesa.setFormaPagamento(FormaPagamento.BOLETO);
        despesa.setConta("Conta 2");

        when(despesaRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(List.of(despesa));

        List<TransacaoDTO> transacoes = transacaoService.listarTransacoes(
                usuario,
                "DESPESA",
                null,
                null,
                null,
                null,
                null
        );

        assertThat(transacoes).hasSize(1);
        assertThat(transacoes.get(0).tipo()).isEqualTo("DESPESA");
        assertThat(transacoes.get(0).valor()).isEqualByComparingTo(BigDecimal.valueOf(-50));
    }

    @Test
    void listarTransacoes_deveFiltrarApenasReceitas() {
        Receita receita = new Receita();
        receita.setId(2L);
        receita.setDescricao("Salario");
        receita.setData(LocalDate.now());
        receita.setValor(BigDecimal.valueOf(2000));
        receita.setCategoria(categoria);
        receita.setUsuario(usuario);
        receita.setFormaPagamento(FormaPagamento.CARTAO_DEBITO);
        receita.setConta("Conta 1");

        when(receitaRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(List.of(receita));

        List<TransacaoDTO> transacoes = transacaoService.listarTransacoes(
                usuario,
                "RECEITA",
                null,
                null,
                null,
                null,
                null
        );

        assertThat(transacoes).hasSize(1);
        assertThat(transacoes.get(0).tipo()).isEqualTo("RECEITA");
        assertThat(transacoes.get(0).valor()).isEqualByComparingTo(BigDecimal.valueOf(2000));
    }
}
