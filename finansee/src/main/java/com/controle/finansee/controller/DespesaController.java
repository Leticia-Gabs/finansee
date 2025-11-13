package com.controle.finansee.controller;

import com.controle.finansee.dto.CriarDespesaResponseDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.controle.finansee.model.user.User;
import com.controle.finansee.dto.DespesaDTO;
import com.controle.finansee.service.DespesaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/despesas")
@CrossOrigin(origins = "*") // Permite que o frontend (de qualquer origem) acesse a API
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @PostMapping
    public ResponseEntity<CriarDespesaResponseDTO> criarDespesa(
            @Valid @RequestBody DespesaDTO despesaDTO,
            @AuthenticationPrincipal User usuario) {
        CriarDespesaResponseDTO novaDespesa = despesaService.criar(despesaDTO, usuario);
        return new ResponseEntity<>(novaDespesa, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DespesaDTO> atualizarDespesa(
            @PathVariable Long id,
            @Valid @RequestBody DespesaDTO despesaDTO,
            @AuthenticationPrincipal User usuario) {

        DespesaDTO despesaAtualizada = despesaService.atualizar(id, despesaDTO, usuario);
        return ResponseEntity.ok(despesaAtualizada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaDTO> buscarDespesaPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal User usuario) {

        DespesaDTO despesa = despesaService.buscarPorId(id, usuario);
        return ResponseEntity.ok(despesa);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDespesa(
            @PathVariable Long id,
            @AuthenticationPrincipal User usuario) {

        despesaService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }

    // NOVO: Endpoint para filtrar despesas com paginação, ordenação e filtros opcionais
    //
    /*
    *@GetMapping("/filtro")
    *public ResponseEntity<Page<DespesaDTO>> filtrarDespesas(
        @RequestParam(required = false) String categoria,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
        @RequestParam(required = false) BigDecimal valorMin,
        @RequestParam(required = false) BigDecimal valorMax,
        @AuthenticationPrincipal User usuario,
        Pageable pageable) {

        Page<DespesaDTO> despesasFiltradas = despesaService.filtrarComPaginacao(
                categoria, dataInicio, dataFim, valorMin, valorMax, usuario, pageable);

    return ResponseEntity.ok(despesasFiltradas);
    }
*/


}

