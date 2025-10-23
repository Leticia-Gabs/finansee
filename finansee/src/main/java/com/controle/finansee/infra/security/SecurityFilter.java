package com.controle.finansee.infra.security;

// Padrão

import com.controle.finansee.model.user.User;
import com.controle.finansee.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    private static final List<String> PUBLIC_PATHS = List.of("/api/auth/login", "/api/auth/register");

    @Override
    protected  void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getServletPath();
        System.out.println("SecurityFilter - Path recebido: " + requestPath); // Log para Debug

        if (PUBLIC_PATHS.contains(requestPath)) {
            System.out.println("SecurityFilter - Path público detectado, pulando validação de token."); // Log para Debug
            filterChain.doFilter(request, response); // Continue to the next filter or controller
            return; // <<< Important: Stop processing THIS filter
        }
        System.out.println("SecurityFilter - Path protegido, processando token..."); // Log para Debug
        var token = this.recoverToken(request);

        if (token != null) { // Only proceed if a token was actually found
            var login = tokenService.validateToken(token); // Validate the found token

            // Only set authentication if token validation returned a valid subject
            if (login != null && !login.isEmpty()){
                // Use the User object directly as the principal for @AuthenticationPrincipal
                User user = userRepository.findByEmail(login)
                        .orElseThrow(() -> new RuntimeException("User Not Found for token subject: " + login));

                // Authorities can be simple for now
                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

                // Pass the User object as the principal
                var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("SecurityFilter - Usuário autenticado: " + login); // Log para Debug
            } else {
                // Optional: Token was present but invalid/expired. Clear context.

                System.out.println("SecurityFilter - Token inválido ou expirado."); // Log para Debug
                SecurityContextHolder.clearContext();
            }
        } else {
            // Optional: No token was present for a protected route. Clear context.
            System.out.println("SecurityFilter - Nenhum token encontrado para path protegido."); // Log para Debug
            SecurityContextHolder.clearContext();
        }

        // Continue the filter chain for all requests (public ones already returned)
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        return authHeader.replace("Bearer ", "");
    }
}
