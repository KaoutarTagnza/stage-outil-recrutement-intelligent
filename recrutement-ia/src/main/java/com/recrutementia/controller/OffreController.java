package com.recrutementia.controller;

import com.recrutementia.dto.OffreRequest;
import com.recrutementia.dto.OffreResponse;
import com.recrutementia.service.OffreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OffreController {

    private final OffreService offreService;

    @GetMapping("/api/offres/publiees")
    public ResponseEntity<List<OffreResponse>> listerOffresPubliees() {
        return ResponseEntity.ok(offreService.listerOffresPubliees());
    }

    @GetMapping("/api/offres/{id}")
    public ResponseEntity<OffreResponse> obtenirOffre(@PathVariable Long id) {
        return ResponseEntity.ok(offreService.obtenirOffre(id));
    }

    @PostMapping("/api/recruteur/offres")
    public ResponseEntity<OffreResponse> creerOffre(@Valid @RequestBody OffreRequest request,
                                                      Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(offreService.creerOffre(request, email));
    }

    @PutMapping("/api/recruteur/offres/{id}/publier")
    public ResponseEntity<OffreResponse> publierOffre(@PathVariable Long id,
                                                         Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(offreService.publierOffre(id, email));
    }

    @GetMapping("/api/recruteur/offres")
    public ResponseEntity<List<OffreResponse>> listerMesOffres(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(offreService.listerMesOffres(email));
    }
}