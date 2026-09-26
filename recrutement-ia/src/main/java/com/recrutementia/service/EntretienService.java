package com.recrutementia.service;

import com.recrutementia.dto.EntretienResponse;
import com.recrutementia.model.*;
import com.recrutementia.repository.CandidatureRepository;
import com.recrutementia.repository.EntretienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntretienService {

    private final CandidatureRepository candidatureRepository;
    private final EntretienRepository entretienRepository;
    private final QuestionGeneratorService questionGeneratorService;
    private final JournalisationService journalisationService;

    public List<String> genererQuestions(Long candidatureId, String emailRecruteur) {
        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new IllegalStateException("Candidature introuvable"));

        verifierProprietaire(candidature, emailRecruteur);

        List<String> nomsCompetences = candidature.getOffre().getCompetences().stream()
                .map(Competence::getNom)
                .collect(Collectors.toList());

        return questionGeneratorService.genererQuestions(candidature.getOffre().getTitre(), nomsCompetences);
    }

    public EntretienResponse planifier(Long candidatureId, LocalDateTime dateEntretien, String emailRecruteur) {
        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new IllegalStateException("Candidature introuvable"));

        verifierProprietaire(candidature, emailRecruteur);

        List<String> questions = genererQuestions(candidatureId, emailRecruteur);

        Entretien entretien = entretienRepository.findAll().stream()
                .filter(e -> e.getCandidature().getId().equals(candidatureId))
                .findFirst()
                .orElseGet(Entretien::new);

        entretien.setCandidature(candidature);
        entretien.setDateEntretien(dateEntretien);
        entretien.setQuestionsGenerees(String.join(" | ", questions));
        entretien = entretienRepository.save(entretien);

        candidature.setStatut(StatutCandidature.ENTRETIEN_PLANIFIE);
        candidatureRepository.save(candidature);

        journalisationService.journaliser(candidature.getOffre().getRecruteur(),
                "Entretien planifie pour la candidature #" + candidatureId);

        return new EntretienResponse(entretien);
    }

    private void verifierProprietaire(Candidature candidature, String emailRecruteur) {
        if (!candidature.getOffre().getRecruteur().getEmail().equals(emailRecruteur)) {
            throw new IllegalStateException("Vous n'etes pas le proprietaire de cette offre");
        }
    }
}