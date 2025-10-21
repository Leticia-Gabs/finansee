package com.controle.finansee.repository;

import com.controle.finansee.model.CategoriaPersonalizada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoriaPersonalizadaRepository extends JpaRepository<CategoriaPersonalizada, Long> {
    boolean existsByNomeAndUsuarioId(String nome, Long usuarioId);

    List<CategoriaPersonalizada> findAllByUsuarioId(Long usuarioId);
}
