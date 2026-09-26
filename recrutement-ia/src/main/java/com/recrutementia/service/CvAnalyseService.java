package com.recrutementia.service;

import com.recrutementia.model.Competence;
import com.recrutementia.model.Cv;
import com.recrutementia.model.Utilisateur;
import com.recrutementia.repository.CvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CvAnalyseService {

    private final PdfTextExtractorService pdfTextExtractorService;
    private final OpenAiCvAnalyseService openAiCvAnalyseService;
    private final CvRepository cvRepository;
    private final JournalisationService journalisationService;

    public void analyser(Cv cv, Utilisateur candidatUtilisateur) {
        try {
            String texteBrut = pdfTextExtractorService.extraireTexte(cv.getFichierUrl());
            CvAnalyseResultat resultat = openAiCvAnalyseService.analyserCv(texteBrut);

            cv.setCompetencesExtraites(String.join(", ", resultat.getCompetences()));
            cv.setExperienceExtraite(resultat.getExperience());
            cv.setResumeIa(resultat.getResume());

            Set<Competence> competencesRequises = cv.getCandidature().getOffre().getCompetences();
            BigDecimal score = calculerScore(resultat.getCompetences(), competencesRequises);
            cv.setScoreMatching(score);

            cvRepository.save(cv);

            journalisationService.journaliser(candidatUtilisateur, "Analyse IA du CV terminee (score: " + score + "/100)");

        } catch (Exception e) {
            journalisationService.journaliser(candidatUtilisateur, "Echec de l'analyse IA du CV : " + e.getMessage());
        }
    }

    private BigDecimal calculerScore(List<String> competencesExtraites, Set<Competence> competencesRequises) {
        if (competencesRequises.isEmpty()) {
            return BigDecimal.ZERO;
        }

        List<String> extraitesEnMinuscule = competencesExtraites.stream()
                .map(String::toLowerCase)
                .toList();

        long nbTrouvees = competencesRequises.stream()
                .filter(c -> extraitesEnMinuscule.stream()
                        .anyMatch(e -> e.contains(c.getNom().toLowerCase())))
                .count();

        BigDecimal pourcentage = BigDecimal.valueOf(nbTrouvees)
                .divide(BigDecimal.valueOf(competencesRequises.size()), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        return pourcentage.setScale(2, RoundingMode.HALF_UP);
    }
}