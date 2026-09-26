package com.recrutementia.controller;

import com.recrutementia.dto.EntretienRequest;
import com.recrutementia.dto.EntretienResponse;
import com.recrutementia.service.EntretienService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruteur/candidatures/{candidatureId}")
@RequiredArgsConstructor
public class EntretienController {

    private final EntretienService entretienService;

    @GetMapping("/questions")
    public ResponseEntity<List<String>> genererQuestions(@PathVariable Long candidatureId,
                                                            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(entretienService.genererQuestions(candidatureId, email));
    }

    @PostMapping("/entretien")
    public ResponseEntity<EntretienResponse> planifierEntretien(@PathVariable Long candidatureId,
                                                                    @Valid @RequestBody EntretienRequest request,
                                                                    Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(entretienService.planifier(candidatureId, request.getDateEntretien(), email));
    }
}