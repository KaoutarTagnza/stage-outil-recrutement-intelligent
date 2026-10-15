
package com.recrutementia.controller;

import com.recrutementia.dto.CandidatureRequest;
import com.recrutementia.dto.CandidatureResponse;
import com.recrutementia.dto.DecisionFinaleRequest;
import com.recrutementia.dto.EntretienResponse;
import com.recrutementia.service.CandidatureService;
import com.recrutementia.service.EntretienService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CandidatureController {

    private final CandidatureService candidatureService;
    private final EntretienService entretienService;

    // ---- Cote candidat ----

    @PostMapping("/api/candidat/candidatures")
    public ResponseEntity<CandidatureResponse> postuler(
            @Valid @RequestBody CandidatureRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                candidatureService.postuler(request.getOffreId(), email)
        );
    }

    @PostMapping(
            value = "/api/candidat/candidatures/{id}/cv",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<CandidatureResponse> deposerCv(
            @PathVariable Long id,
            @RequestParam("fichier") MultipartFile fichier,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                candidatureService.deposerCv(id, fichier, email)
        );
    }

    @GetMapping("/api/candidat/candidatures")
    public ResponseEntity<List<CandidatureResponse>> mesCandidatures(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                candidatureService.listerMesCandidatures(email)
        );
    }

    // ---- Entretiens du candidat ----

    @GetMapping("/api/candidat/entretiens")
    public ResponseEntity<List<EntretienResponse>> mesEntretiens(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                entretienService.listerMesEntretiens(email)
        );
    }

    // ---- Cote recruteur ----

    @GetMapping("/api/recruteur/offres/{offreId}/candidatures")
    public ResponseEntity<List<CandidatureResponse>> candidaturesPourOffre(
            @PathVariable Long offreId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                candidatureService.listerCandidaturesOffre(offreId, email)
        );
    }

    @PutMapping("/api/recruteur/candidatures/{id}/decision")
    public ResponseEntity<CandidatureResponse> deciderFinal(
            @PathVariable Long id,
            @Valid @RequestBody DecisionFinaleRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                candidatureService.deciderFinal(
                        id,
                        request.getAccepter(),
                        request.getJustification(),
                        email
                )
        );
    }
}
