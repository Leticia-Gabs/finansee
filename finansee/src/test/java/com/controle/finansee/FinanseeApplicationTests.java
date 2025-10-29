package com.controle.finansee;

import com.controle.finansee.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class FinanseeApplicationTests {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testeMockado() {
        // Exemplo de mock simples
        when(userRepository.findByEmail("teste@email.com")).thenReturn(null);

        // Aqui você pode chamar algum serviço que use o UserRepository
        assert(userRepository.findByEmail("teste@email.com") == null);

        verify(userRepository).findByEmail("teste@email.com");
    }
}
