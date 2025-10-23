package com.controle.finansee.repository;

import com.controle.finansee.model.Receita; // Import Receita
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // Importe
import java.util.List;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long>, JpaSpecificationExecutor<Receita> {

    List<Receita> findAllByUsuarioId(Long usuarioId);
}