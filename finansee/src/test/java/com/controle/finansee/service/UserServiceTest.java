package com.controle.finansee.service;

import com.controle.finansee.dto.LoginRequestDTO;
import com.controle.finansee.dto.RegisterRequestDTO;
import com.controle.finansee.dto.ResponseDTO;
import com.controle.finansee.infra.security.TokenService;
import com.controle.finansee.model.CategoriaPersonalizada;
import com.controle.finansee.model.TipoCategoria;
import com.controle.finansee.model.user.User;
import com.controle.finansee.repository.CategoriaPersonalizadaRepository;
import com.controle.finansee.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoriaPersonalizadaRepository categoriaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmail("teste@email.com");
        user.setName("Usuário Teste");
        user.setPassword("senhaCodificada");
    }

    // ✅ LOGIN COM SUCESSO
    @Test
    void deveFazerLoginComSucesso() {
        LoginRequestDTO loginDTO = new LoginRequestDTO("teste@email.com", "123456");

        when(userRepository.findByEmail(loginDTO.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.password(), user.getPassword())).thenReturn(true);
        when(tokenService.generateToken(user)).thenReturn("fake-token");

        ResponseDTO resposta = userService.loginUser(loginDTO);

        assertNotNull(resposta);
        assertEquals("Usuário Teste", resposta.name());
        assertEquals("fake-token", resposta.token());

        verify(userRepository).findByEmail(loginDTO.email());
        verify(tokenService).generateToken(user);
    }

    // ❌ LOGIN COM SENHA INCORRETA
    @Test
    void deveFalharLoginComSenhaIncorreta() {
        LoginRequestDTO loginDTO = new LoginRequestDTO("teste@email.com", "errada");

        when(userRepository.findByEmail(loginDTO.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.password(), user.getPassword())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> userService.loginUser(loginDTO));
    }

    // ❌ LOGIN COM USUÁRIO INEXISTENTE
    @Test
    void deveFalharLoginComUsuarioInexistente() {
        LoginRequestDTO loginDTO = new LoginRequestDTO("naoexiste@email.com", "123");

        when(userRepository.findByEmail(loginDTO.email())).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> userService.loginUser(loginDTO));
    }

    // ✅ REGISTRO DE NOVO USUÁRIO
    @Test
    void deveRegistrarNovoUsuarioComSucesso() {
        RegisterRequestDTO registerDTO = new RegisterRequestDTO("Novo Usuário", "novo@email.com", "123456");

        when(userRepository.findByEmail(registerDTO.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerDTO.password())).thenReturn("senhaCodificada");
        when(tokenService.generateToken(any(User.class))).thenReturn("token-novo");

        ResponseDTO resposta = userService.registerUser(registerDTO);

        assertNotNull(resposta);
        assertEquals("Novo Usuário", resposta.name());
        assertEquals("token-novo", resposta.token());

        verify(userRepository).save(any(User.class));
        verify(categoriaRepository).saveAll(anyList());
        verify(tokenService).generateToken(any(User.class));
    }

    // ❌ REGISTRO COM E-MAIL DUPLICADO
    @Test
    void deveFalharRegistroQuandoEmailJaExiste() {
        RegisterRequestDTO registerDTO = new RegisterRequestDTO("Usuário", "teste@email.com", "123");

        when(userRepository.findByEmail(registerDTO.email())).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(registerDTO));
        verify(userRepository, never()).save(any());
    }

    // ✅ VERIFICA SE AS CATEGORIAS PADRÃO SÃO CRIADAS CORRETAMENTE
    @Test
    void deveCriarCategoriasPadraoCorretamente() {
        // Chama o método privado via reflexão
        assertDoesNotThrow(() -> {
            var method = UserService.class.getDeclaredMethod("createDefaultCategoriesForUser", User.class);
            method.setAccessible(true);
            method.invoke(userService, user);
        });

        // Verifica se o método saveAll foi chamado
        verify(categoriaRepository).saveAll(ArgumentMatchers.argThat(lista -> {
            List<CategoriaPersonalizada> categorias = (List<CategoriaPersonalizada>) lista;
            return categorias.size() == 9
                    && categorias.stream().anyMatch(c -> c.getTipo() == TipoCategoria.DESPESA)
                    && categorias.stream().anyMatch(c -> c.getTipo() == TipoCategoria.RECEITA);
        }));
    }
}
