package com.controle.finansee.repository;

import com.controle.finansee.dto.GastoPorCategoriaDTO;
import com.controle.finansee.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // Importe

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long>, JpaSpecificationExecutor<Despesa> {

    List<Despesa> findAllByUsuarioId(Long usuarioId);

    @Query( "SELECT new com.controle.finansee.dto.GastoPorCategoriaDTO(c.nome, c.cor, SUM(d.valor))" +
            "FROM Despesa d JOIN d.categoria c " +
            "WHERE d.usuario.id = :usuarioId "   +
            "AND YEAR(d.data)   = :ano "   +
            "AND MONTH(d.data)  = :mes "   +
            "GROUP BY c.nome, c.cor " +
            "ORDER BY SUM(d.valor) DESC"
    )
    List<GastoPorCategoriaDTO> findGastosPorCategoria(
            @Param("usuarioId") Long usuarioId,
            @Param("ano") int ano,
            @Param("mes") int mes
    );

    // NOVO: Soma TODAS as despesas do usuário
    @Query("SELECT SUM(d.valor) FROM Despesa d WHERE d.usuario.id = :usuarioId")
    BigDecimal sumTotalByUsuarioId(@Param("usuarioId") Long usuarioId);

    // ATUALIZAR ESTE (se ainda não existir): Agrega TODAS as despesas por categoria
    @Query("SELECT new com.controle.finansee.dto.GastoPorCategoriaDTO(c.nome, c.cor, SUM(d.valor)) " +
            "FROM Despesa d JOIN d.categoria c " +
            "WHERE d.usuario.id = :usuarioId " +
            "GROUP BY c.nome, c.cor " +
            "ORDER BY SUM(d.valor) DESC")
    List<GastoPorCategoriaDTO> findGastosPorCategoriaParaDashboard(@Param("usuarioId") Long usuarioId);
}