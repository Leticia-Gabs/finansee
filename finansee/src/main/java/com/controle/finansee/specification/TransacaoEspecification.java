package com.controle.finansee.specification; // Ajuste o pacote

import com.controle.finansee.model.CategoriaPersonalizada;
import com.controle.finansee.model.Despesa; // Importe Despesa
import com.controle.finansee.model.Receita; // Importe Receita
import com.controle.finansee.model.user.User; // Importe User
import jakarta.persistence.criteria.*; // Import necessário
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransacaoEspecification {

    // Specification genérica que pode ser usada para Despesa ou Receita
    // T representa a entidade (Despesa ou Receita)
    public static <T> Specification<T> RASCUNHO(
                                                 User usuario,
                                                 Long categoriaId,
                                                 LocalDate dataInicio,
                                                 LocalDate dataFim,
                                                 BigDecimal valorMin,
                                                 BigDecimal valorMax) {

        // Retorna uma lambda que implementa a interface Specification
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // 1. Filtro OBRIGATÓRIO por usuário
            Join<T, User> userJoin = root.join("usuario");
            predicates.add(cb.equal(userJoin.get("id"), usuario.getId()));

            // 2. Filtro OPCIONAL por categoriaId
            if (categoriaId != null) {
                Join<T, CategoriaPersonalizada> categoriaJoin = root.join("categoria"); // Assume campo 'categoria'
                predicates.add(cb.equal(categoriaJoin.get("id"), categoriaId));
            }

            // 3. Filtro OPCIONAL por dataInicio (>=)
            if (dataInicio != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("data"), dataInicio)); // Assume campo 'data'
            }

            // 4. Filtro OPCIONAL por dataFim (<=)
            if (dataFim != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("data"), dataFim));
            }

            // 5. Filtro OPCIONAL por valorMin (>=)
            if (valorMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("valor"), valorMin)); // Assume campo 'valor'
            }

            // 6. Filtro OPCIONAL por valorMax (<=)
            if (valorMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("valor"), valorMax));
            }

            // Combina todos os predicates com AND
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}