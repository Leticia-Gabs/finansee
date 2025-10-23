package com.controle.finansee.repository;

import com.controle.finansee.model.Receita; // Import Receita
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    List<Receita> findAllByUsuarioId(Long usuarioId);

    @Query("SELECT r FROM Receita r JOIN r.usuario u JOIN r.categoria c WHERE u.id = :usuarioId " + // Mudado para Receita (r)
            "AND (:categoriaId IS NULL OR c.id = :categoriaId) " +
            "AND (:dataInicio IS NULL OR r.data >= :dataInicio) " +
            "AND (:dataFim IS NULL OR r.data <= :dataFim) " +
            "AND (:valorMin IS NULL OR r.valor >= :valorMin) " +
            "AND (:valorMax IS NULL OR r.valor <= :valorMax) " +
            "ORDER BY r.data DESC, r.id DESC") // Ordena por data (mais recente) e depois ID
    List<Receita> findByFilters( // Mudado para List<Receita>
                                 @Param("usuarioId") Long usuarioId,
                                 @Param("categoriaId") Long categoriaId,
                                 @Param("dataInicio") LocalDate dataInicio,
                                 @Param("dataFim") LocalDate dataFim,
                                 @Param("valorMin") BigDecimal valorMin,
                                 @Param("valorMax") BigDecimal valorMax
    );

}