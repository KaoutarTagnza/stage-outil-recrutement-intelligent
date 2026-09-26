package com.recrutementia.controller;

import com.recrutementia.dto.*;
import com.recrutementia.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/utilisateurs")
    public ResponseEntity<List<UtilisateurResponse>> listerUtilisateurs() {
        return ResponseEntity.ok(adminService.listerUtilisateurs());
    }

    @PutMapping("/utilisateurs/{id}/statut")
    public ResponseEntity<UtilisateurResponse> changerStatut(@PathVariable Long id,
                                                                @Valid @RequestBody ChangerStatutRequest request,
                                                                Authentication authentication) {
        return ResponseEntity.ok(adminService.changerStatut(id, request.getActif(), authentication.getName()));
    }

    @PutMapping("/utilisateurs/{id}/role")
    public ResponseEntity<UtilisateurResponse> changerRole(@PathVariable Long id,
                                                              @Valid @RequestBody ChangerRoleRequest request,
                                                              Authentication authentication) {
        return ResponseEntity.ok(adminService.changerRole(id, request.getRole(), authentication.getName()));
    }

    @GetMapping("/logs")
    public ResponseEntity<List<LogActionResponse>> listerLogs() {
        return ResponseEntity.ok(adminService.listerLogs());
    }

    @GetMapping("/statistiques")
    public ResponseEntity<StatistiquesResponse> statistiques() {
        return ResponseEntity.ok(adminService.obtenirStatistiques());
    }
}