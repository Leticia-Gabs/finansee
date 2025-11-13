package com.controle.finansee.controller;

import com.controle.finansee.dto.DashboardDataDTO;
import com.controle.finansee.model.user.User;
import com.controle.finansee.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardDataDTO> getDashboardData(
            @AuthenticationPrincipal User usuario
    ) {
        DashboardDataDTO data = dashboardService.getDashboardData(usuario);
        return ResponseEntity.ok(data);
    }
}