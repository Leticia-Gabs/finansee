package com.controle.finansee.repository;

import com.controle.finansee.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    List<Receita> findAllByUsuarioId(Long usuarioId);
}