
package com.controle.finansee.controller;

import com.controle.finansee.dto.DespesaDTO;
import com.controle.finansee.service.DespesaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/despesas")
@CrossOrigin(origins = "*") // Permite que o frontend (de qualquer origem) acesse a API
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @PostMapping
    public ResponseEntity<DespesaDTO> criarDespesa(@Valid @RequestBody DespesaDTO despesaDTO) {
        DespesaDTO novaDespesa = despesaService.criar(despesaDTO);
        // Retorna o status 201 Created com a despesa criada no corpo da resposta
        return new ResponseEntity<>(novaDespesa, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DespesaDTO>> listarTodasDespesas() {
        List<DespesaDTO> despesas = despesaService.listarTodas();
        return ResponseEntity.ok(despesas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaDTO> buscarDespesaPorId(@PathVariable Long id) {
        DespesaDTO despesa = despesaService.buscarPorId(id);
        return ResponseEntity.ok(despesa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DespesaDTO> atualizarDespesa(@PathVariable Long id, @Valid @RequestBody DespesaDTO despesaDTO) {
        DespesaDTO despesaAtualizada = despesaService.atualizar(id, despesaDTO);
        return ResponseEntity.ok(despesaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDespesa(@PathVariable Long id) {
        despesaService.deletar(id);
        // Retorna o status 204 No Content, indicando sucesso sem corpo de resposta
        return ResponseEntity.noContent().build();
    }
}

