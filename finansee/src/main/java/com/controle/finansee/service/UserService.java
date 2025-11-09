package com.controle.finansee.service;

import com.controle.finansee.dto.LoginRequestDTO;
import com.controle.finansee.dto.RegisterRequestDTO;
import com.controle.finansee.dto.ResponseDTO;
import com.controle.finansee.model.TipoCategoria; // Importe seu Enum
import com.controle.finansee.infra.security.TokenService;
import com.controle.finansee.model.CategoriaPersonalizada; // Importe sua Categoria
import com.controle.finansee.model.user.User; // Importe seu User
import com.controle.finansee.repository.CategoriaPersonalizadaRepository; // Importe o Repo
import com.controle.finansee.repository.UserRepository;
import jakarta.transaction.Transactional; // Import importante
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CategoriaPersonalizadaRepository categoriaRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public ResponseDTO loginUser(LoginRequestDTO body) {
        User user = userRepository.findByEmail(body.email())
                .orElseThrow(() -> new BadCredentialsException("Usuário ou senha inválidos."));

        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = tokenService.generateToken(user);
            return new ResponseDTO(user.getName(), token);
        }

        // Lança a mesma exceção se a senha estiver errada
        throw new BadCredentialsException("Usuário ou senha inválidos.");
    }

    @Transactional
    public ResponseDTO registerUser(RegisterRequestDTO body) {
        Optional<User> existingUser = userRepository.findByEmail(body.email());

        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(body.password()));
        newUser.setEmail(body.email());
        newUser.setName(body.name());
        userRepository.save(newUser);

        // Criar as categorias padrão para este usuário
        createDefaultCategoriesForUser(newUser);

        // Gerar e retornar o token
        String token = tokenService.generateToken(newUser);
        return new ResponseDTO(newUser.getName(), token);
    }

    private void createDefaultCategoriesForUser(User user) {
        List<CategoriaPersonalizada> defaultCategories = List.of(
                // --- Despesas Padrão ---
                new CategoriaPersonalizada(null, "Alimentação", TipoCategoria.DESPESA, "#FF6347", user),
                new CategoriaPersonalizada(null, "Transporte", TipoCategoria.DESPESA, "#4682B4", user),
                new CategoriaPersonalizada(null, "Moradia", TipoCategoria.DESPESA, "#B22222", user),
                new CategoriaPersonalizada(null, "Lazer", TipoCategoria.DESPESA, "#9370DB", user),
                new CategoriaPersonalizada(null, "Saúde", TipoCategoria.DESPESA, "#FF4500", user),
                new CategoriaPersonalizada(null, "Outras Despesas", TipoCategoria.DESPESA, "#A9A9A9", user),

                // --- Receitas Padrão ---
                new CategoriaPersonalizada(null, "Salário", TipoCategoria.RECEITA, "#3CB371", user),
                new CategoriaPersonalizada(null, "Renda Extra", TipoCategoria.RECEITA, "#FFD700", user),
                new CategoriaPersonalizada(null, "Outras Receitas", TipoCategoria.RECEITA, "#90EE90", user)
        );

        categoriaRepository.saveAll(defaultCategories);
    }
}
