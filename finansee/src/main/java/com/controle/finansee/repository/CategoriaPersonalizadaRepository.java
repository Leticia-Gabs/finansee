package com.controle.finansee.repository;

import com.controle.finansee.model.CategoriaPersonalizada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaPersonalizadaRepository extends JpaRepository<CategoriaPersonalizada, Long> {
    boolean existsByNome(String nome);
}
