package com.controle.finansee.controller;

import com.controle.finansee.dto.TransacaoDTO;
import com.controle.finansee.model.user.User; // Importe sua entidade User
import com.controle.finansee.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat; // Import necessário
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal; // Import necessário
import java.time.LocalDate; // Import necessário
import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
@CrossOrigin(origins = "*") // Lembre-se de restringir em produção
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @GetMapping
    public ResponseEntity<List<TransacaoDTO>> listarTransacoes(
            @AuthenticationPrincipal User usuario, // Usuário logado

            // --- NOVOS PARÂMETROS DE FILTRO ---
            @RequestParam(required = false) Long categoriaId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // Garante formato AAAA-MM-DD
            LocalDate dataInicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataFim,

            @RequestParam(required = false) BigDecimal valorMin,
            @RequestParam(required = false) BigDecimal valorMax,

            @RequestParam(required = false) String tipo
            // ------------------------------------
    ) {

        // Passa os filtros para o service
        List<TransacaoDTO> transacoes = transacaoService.listarTransacoes(
                usuario, tipo, categoriaId, dataInicio, dataFim, valorMin, valorMax
        );
        return ResponseEntity.ok(transacoes);
    }
}