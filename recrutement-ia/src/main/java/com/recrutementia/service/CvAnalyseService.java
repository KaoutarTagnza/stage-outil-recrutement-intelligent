
package com.recrutementia.service;

import com.recrutementia.model.Competence;
import com.recrutementia.model.Cv;
import com.recrutementia.model.Utilisateur;
import com.recrutementia.repository.CvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CvAnalyseService {

    private final PdfTextExtractorService pdfTextExtractorService;
    private final OpenAiCvAnalyseService openAiCvAnalyseService;
    private final CvRepository cvRepository;
    private final JournalisationService journalisationService;

    @Transactional(noRollbackFor = IllegalStateException.class)
    public void analyser(Cv cv, Utilisateur candidatUtilisateur) {

        BigDecimal score;

        try {
            // 1. Extraire le texte du PDF
            String texteBrut =
                    pdfTextExtractorService.extraireTexte(cv.getFichierUrl());

            if (texteBrut == null || texteBrut.isBlank()) {
                throw new IllegalStateException(
                        "Aucun texte n'a ete extrait du CV PDF. "
                        + "Essayez un PDF dont le texte peut etre selectionne."
                );
            }

            // 2. Recuperer les competences requises par l'offre
            Set<Competence> competencesRequises =
                    cv.getCandidature().getOffre().getCompetences();

            if (competencesRequises == null
                    || competencesRequises.isEmpty()) {
                throw new IllegalStateException(
                        "Cette offre n'a aucune competence requise. "
                        + "Ajoutez des competences a l'offre avant "
                        + "de relancer l'analyse du CV."
                );
            }

            // 3. Analyser le CV
            CvAnalyseResultat resultat =
                    openAiCvAnalyseService.analyserCv(texteBrut);

            Set<String> competencesDetectees = new LinkedHashSet<>();

            if (resultat.getCompetences() != null) {
                competencesDetectees.addAll(resultat.getCompetences());
            }

            // 4. Diagnostic : verifier les competences une par une
            System.out.println("\n===== DIAGNOSTIC DU MATCHING =====");
            System.out.println("Fichier CV : " + cv.getFichierUrl());
            System.out.println(
                    "Longueur du texte extrait : " + texteBrut.length()
            );
            System.out.println(
                    "Nombre de competences requises : "
                    + competencesRequises.size()
            );

            long nbTrouvees = 0;

            for (Competence requise : competencesRequises) {

                String nomCompetence = requise.getNom();

                boolean trouvee =
                        OpenAiCvAnalyseService.contientCompetence(
                                texteBrut, nomCompetence
                        );

                System.out.println(
                        "Competence : " + nomCompetence
                        + " | Trouvee : " + trouvee
                );

                if (trouvee) {
                    nbTrouvees++;

                    // Ajouter la competence si elle n'est pas deja detectee
                    boolean dejaPresente = competencesDetectees.stream()
                            .anyMatch(c ->
                                    c.equalsIgnoreCase(nomCompetence)
                            );

                    if (!dejaPresente) {
                        competencesDetectees.add(nomCompetence);
                    }
                }
            }

            // 5. Calculer le score de correspondance
            score = BigDecimal.valueOf(nbTrouvees)
                    .divide(
                            BigDecimal.valueOf(
                                    competencesRequises.size()
                            ),
                            4,
                            RoundingMode.HALF_UP
                    )
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);

            System.out.println(
                    "Competences trouvees : " + nbTrouvees
                    + " / " + competencesRequises.size()
            );
            System.out.println("Score calcule : " + score + " %");
            System.out.println("===== FIN DU DIAGNOSTIC =====\n");

            // 6. Enregistrer les resultats
            cv.setCompetencesExtraites(
                    String.join(", ", competencesDetectees)
            );

            cv.setExperienceExtraite(resultat.getExperience());
            cv.setResumeIa(resultat.getResume());
            cv.setScoreMatching(score);

            cvRepository.save(cv);

        } catch (Exception e) {

            // En cas d'echec, ne pas conserver un faux score de 0
            try {
                cv.setScoreMatching(null);
                cvRepository.save(cv);
            } catch (Exception ignored) {
                // Conserver l'erreur initiale
            }

            try {
                journalisationService.journaliser(
                        candidatUtilisateur,
                        "Echec de l'analyse du CV : " + e.getMessage()
                );
            } catch (Exception ignored) {
                // Ne pas masquer l'erreur principale
            }

            String message = e.getMessage() == null
                    ? "Erreur inconnue pendant l'analyse du PDF."
                    : e.getMessage();

            throw new IllegalStateException(
                    "Analyse du CV impossible : " + message,
                    e
            );
        }

        // 7. Journaliser le resultat sans affecter le score
        try {
            journalisationService.journaliser(
                    candidatUtilisateur,
                    "Analyse du CV terminee (score : " + score + "/100)"
            );
        } catch (Exception ignored) {
            // Le score a deja ete enregistre
        }
    }
}
