package com.recrutementia.controller;

import com.recrutementia.dto.EvaluationRequest;
import com.recrutementia.dto.EvaluationResponse;
import com.recrutementia.service.EvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruteur/entretiens/{entretienId}/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<EvaluationResponse> evaluer(@PathVariable Long entretienId,
                                                          @Valid @RequestBody EvaluationRequest request,
                                                          Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(evaluationService.evaluer(entretienId, request, email));
    }
}