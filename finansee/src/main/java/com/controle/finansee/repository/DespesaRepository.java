package com.controle.finansee.repository;

import com.controle.finansee.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Esta interface herda de JpaRepository,
// e o Spring Data JPA implementará
// todos os métodos básicos de CRUD para nós.

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {
}