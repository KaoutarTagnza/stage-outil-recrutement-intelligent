package com.recrutementia.service;

import com.recrutementia.dto.EvaluationRequest;
import com.recrutementia.dto.EvaluationResponse;
import com.recrutementia.model.*;
import com.recrutementia.repository.EntretienRepository;
import com.recrutementia.repository.EvaluationRepository;
import com.recrutementia.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EntretienRepository entretienRepository;
    private final EvaluationRepository evaluationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final JournalisationService journalisationService;

    public EvaluationResponse evaluer(Long entretienId, EvaluationRequest req, String emailRecruteur) {
        Entretien entretien = entretienRepository.findById(entretienId)
                .orElseThrow(() -> new IllegalStateException("Entretien introuvable"));

        Utilisateur recruteur = utilisateurRepository.findByEmail(emailRecruteur)
                .orElseThrow(() -> new IllegalStateException("Recruteur introuvable"));

        if (!entretien.getCandidature().getOffre().getRecruteur().getEmail().equals(emailRecruteur)) {
            throw new IllegalStateException("Vous n'etes pas le proprietaire de cette offre");
        }

        Evaluation evaluation = evaluationRepository.findAll().stream()
                .filter(e -> e.getEntretien().getId().equals(entretienId))
                .findFirst()
                .orElseGet(Evaluation::new);

        evaluation.setEntretien(entretien);
        evaluation.setRecruteur(recruteur);
        evaluation.setNoteGlobale(req.getNoteGlobale());
        evaluation.setCommentaire(req.getCommentaire());
        evaluation.setDecision(req.getDecision());
        evaluation = evaluationRepository.save(evaluation);

        journalisationService.journaliser(recruteur, "Evaluation enregistree pour l'entretien #" + entretienId
                + " (decision: " + req.getDecision() + ")");

        return new EvaluationResponse(evaluation);
    }
}