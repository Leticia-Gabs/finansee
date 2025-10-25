package com.controle.finansee;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // 🔹 ativa o perfil de teste
class FinanseeApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("✅ Contexto carregado com sucesso!");
    }

}
