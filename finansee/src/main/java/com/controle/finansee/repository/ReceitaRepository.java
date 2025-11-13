package com.controle.finansee.repository;

import com.controle.finansee.dto.ReceitaPorCategoriaDTO;
import com.controle.finansee.model.Receita; // Import Receita
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // Importe
import java.util.List;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long>, JpaSpecificationExecutor<Receita> {

    List<Receita> findAllByUsuarioId(Long usuarioId);

    @Query("SELECT SUM(r.valor) FROM Receita r " +
            "WHERE r.usuario.id = :usuarioId " +
            "AND YEAR(r.data) = :ano " +
            "AND MONTH(r.data) = :mes")
    BigDecimal sumByUsuarioAndMes(
            @Param("usuarioId") Long usuarioId,
            @Param("ano") int ano,
            @Param("mes") int mes
    );

    @Query("SELECT SUM(r.valor) FROM Receita r WHERE r.usuario.id = :usuarioId")
    BigDecimal sumTotalByUsuarioId(@Param("usuarioId") Long usuarioId);

    // NOVO: Agrega receitas por categoria (para o gráfico do Dashboard)
    @Query("SELECT new com.controle.finansee.dto.ReceitaPorCategoriaDTO(c.nome, c.cor, SUM(r.valor)) " +
            "FROM Receita r JOIN r.categoria c " +
            "WHERE r.usuario.id = :usuarioId " +
            "GROUP BY c.nome, c.cor " +
            "ORDER BY SUM(r.valor) DESC")
    List<ReceitaPorCategoriaDTO> findReceitasPorCategoria(@Param("usuarioId") Long usuarioId);
}