package com.controle.finansee.controller;

import com.controle.finansee.dto.GastoPorCategoriaDTO;
import com.controle.finansee.model.user.User;
import com.controle.finansee.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")

public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/gastos-por-categoria")
    public ResponseEntity<List<GastoPorCategoriaDTO>> getGastosPorCategoria(
            @AuthenticationPrincipal User usuario,
            @RequestParam int ano,
            @RequestParam int mes
    ) {
        List<GastoPorCategoriaDTO> relatorio = relatorioService.getGastosPorCategoria(usuario, ano, mes);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/exportar-pdf")
    public ResponseEntity<Resource> exportarTransacoes(
            @AuthenticationPrincipal User usuario,
            @RequestParam int ano,
            @RequestParam int mes
    ) {
        try {
            String filename = String.format("relatorio_%d_%02d.pdf", ano, mes);
            ByteArrayInputStream pdfStream = relatorioService.gerarPdfTransacoes(usuario, ano, mes);

            InputStreamResource resource = new InputStreamResource(pdfStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_PDF) // Define o tipo de conteúdo como PDF
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
