package com.controle.finansee.controller;

import com.controle.finansee.dto.TransacaoDTO;
import com.controle.finansee.model.user.User;
import com.controle.finansee.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
@CrossOrigin(origins = "*")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @GetMapping
    public ResponseEntity<List<TransacaoDTO>> listarTransacoes(
            @AuthenticationPrincipal User usuario) {

        List<TransacaoDTO> transacoes = transacaoService.listarTransacoes(usuario);
        return ResponseEntity.ok(transacoes);
    }
}