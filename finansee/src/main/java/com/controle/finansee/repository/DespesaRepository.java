package com.controle.finansee.repository;

import com.controle.finansee.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    List<Despesa> findAllByUsuarioId(Long usuarioId);


    @Query("SELECT d FROM Despesa d JOIN d.usuario u JOIN d.categoria c WHERE u.id = :usuarioId " +
            "AND (:categoriaId IS NULL OR c.id = :categoriaId) " +
            "AND (:dataInicio IS NULL OR d.data >= :dataInicio) " +
            "AND (:dataFim IS NULL OR d.data <= :dataFim) " +
            "AND (:valorMin IS NULL OR d.valor >= :valorMin) " +
            "AND (:valorMax IS NULL OR d.valor <= :valorMax) " +
            "ORDER BY d.data DESC, d.id DESC") // Ordena por data (mais recente) e depois ID
    List<Despesa> findByFilters(
            @Param("usuarioId") Long usuarioId,
            @Param("categoriaId") Long categoriaId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("valorMin") BigDecimal valorMin,
            @Param("valorMax") BigDecimal valorMax
    );
    // -----------------------------
}