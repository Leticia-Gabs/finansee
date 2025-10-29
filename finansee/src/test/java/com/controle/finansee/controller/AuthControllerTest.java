package com.controle.finansee.controller;

import com.controle.finansee.dto.LoginRequestDTO;
import com.controle.finansee.dto.RegisterRequestDTO;
import com.controle.finansee.dto.ResponseDTO;
import com.controle.finansee.infra.security.TokenService;
import com.controle.finansee.model.user.User;
import com.controle.finansee.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthController authController;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("teste@email.com");
        user.setPassword("senhaCriptografada");
        user.setName("Usuário Teste");
    }

    @Test
    void deveFazerLoginComSucesso() {
        LoginRequestDTO loginDTO = new LoginRequestDTO("teste@email.com", "1234");

        when(userRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("1234", "senhaCriptografada")).thenReturn(true);
        when(tokenService.generateToken(user)).thenReturn("tokenFake123");

        ResponseEntity<?> resposta = authController.login(loginDTO);

        assertEquals(200, resposta.getStatusCodeValue());
        ResponseDTO corpo = (ResponseDTO) resposta.getBody();
        assertNotNull(corpo);
        assertEquals("Usuário Teste", corpo.name());
        assertEquals("tokenFake123", corpo.token());

        verify(userRepository).findByEmail("teste@email.com");
        verify(tokenService).generateToken(user);
    }

    @Test
    void deveRetornarBadRequestSeSenhaIncorreta() {
        LoginRequestDTO loginDTO = new LoginRequestDTO("teste@email.com", "errada");

        when(userRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("errada", "senhaCriptografada")).thenReturn(false);

        ResponseEntity<?> resposta = authController.login(loginDTO);

        assertEquals(400, resposta.getStatusCodeValue());
        verify(tokenService, never()).generateToken(any());
    }

    @Test
    void deveRegistrarUsuarioComSucesso() {
        RegisterRequestDTO registerDTO = new RegisterRequestDTO("novo@teste.com", "1234", "Novo Usuário");

        when(userRepository.findByEmail("novo@teste.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("senhaCriptografada");
        when(tokenService.generateToken(any(User.class))).thenReturn("tokenFake123");

        ResponseEntity<?> resposta = authController.login(registerDTO);

        assertEquals(200, resposta.getStatusCodeValue());
        ResponseDTO corpo = (ResponseDTO) resposta.getBody();
        assertEquals("novo@teste.com", corpo.name());
        assertEquals("tokenFake123", corpo.token());

        verify(userRepository).save(any(User.class));
    }


}
