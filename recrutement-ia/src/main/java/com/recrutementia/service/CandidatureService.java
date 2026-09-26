package com.recrutementia.service;

import com.recrutementia.dto.CandidatureResponse;
import com.recrutementia.model.*;
import com.recrutementia.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidatureService {

    private final CandidatureRepository candidatureRepository;
    private final CandidatRepository candidatRepository;
    private final OffreRepository offreRepository;
    private final CvRepository cvRepository;
    private final JournalisationService journalisationService;
    private final FileStorageService fileStorageService;
    private final CvAnalyseService cvAnalyseService;

    public CandidatureResponse postuler(Long offreId, String emailCandidat) {
        Candidat candidat = candidatRepository.findByUtilisateurEmail(emailCandidat)
                .orElseThrow(() -> new IllegalStateException("Candidat introuvable"));

        Offre offre = offreRepository.findById(offreId)
                .orElseThrow(() -> new IllegalStateException("Offre introuvable"));

        if (offre.getStatut() != StatutOffre.PUBLIEE) {
            throw new IllegalStateException("Cette offre n'est pas ouverte aux candidatures");
        }

        candidatureRepository.findByCandidatIdAndOffreId(candidat.getId(), offreId)
                .ifPresent(c -> { throw new IllegalStateException("Vous avez deja postule a cette offre"); });

        Candidature candidature = new Candidature();
        candidature.setCandidat(candidat);
        candidature.setOffre(offre);
        candidature.setStatut(StatutCandidature.EN_ATTENTE);
        candidature = candidatureRepository.save(candidature);

        journalisationService.journaliser(candidat.getUtilisateur(), "Candidature #" + candidature.getId()
                + " deposee pour l'offre #" + offre.getId());

        return toResponse(candidature);
    }

    public CandidatureResponse deposerCv(Long candidatureId, MultipartFile fichier, String emailCandidat) {
        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new IllegalStateException("Candidature introuvable"));

        if (!candidature.getCandidat().getUtilisateur().getEmail().equals(emailCandidat)) {
            throw new IllegalStateException("Cette candidature ne vous appartient pas");
        }

        String cheminFichier = fileStorageService.storeCv(fichier);

        Cv cv = cvRepository.findByCandidatureId(candidatureId).orElseGet(Cv::new);
        cv.setCandidature(candidature);
        cv.setFichierUrl(cheminFichier);
        cvRepository.save(cv);

        journalisationService.journaliser(candidature.getCandidat().getUtilisateur(),
                "CV depose pour la candidature #" + candidature.getId());

        cvAnalyseService.analyser(cv, candidature.getCandidat().getUtilisateur());

        return toResponse(candidature);
    }

    public List<CandidatureResponse> listerMesCandidatures(String emailCandidat) {
        Candidat candidat = candidatRepository.findByUtilisateurEmail(emailCandidat)
                .orElseThrow(() -> new IllegalStateException("Candidat introuvable"));
        return candidatureRepository.findByCandidatId(candidat.getId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<CandidatureResponse> listerCandidaturesOffre(Long offreId, String emailRecruteur) {
        Offre offre = offreRepository.findById(offreId)
                .orElseThrow(() -> new IllegalStateException("Offre introuvable"));

        if (!offre.getRecruteur().getEmail().equals(emailRecruteur)) {
            throw new IllegalStateException("Vous n'etes pas le proprietaire de cette offre");
        }

        return candidatureRepository.findByOffreId(offreId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CandidatureResponse deciderFinal(Long candidatureId, boolean accepter, String justification, String emailRecruteur) {
        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new IllegalStateException("Candidature introuvable"));

        if (!candidature.getOffre().getRecruteur().getEmail().equals(emailRecruteur)) {
            throw new IllegalStateException("Vous n'etes pas le proprietaire de cette offre");
        }

        candidature.setStatut(accepter ? StatutCandidature.ACCEPTEE : StatutCandidature.REJETEE);
        candidature.setJustification(justification);
        candidatureRepository.save(candidature);

        journalisationService.journaliser(candidature.getOffre().getRecruteur(),
                "Decision " + candidature.getStatut() + " pour la candidature #" + candidatureId
                        + " - Justification : " + justification);

        return toResponse(candidature);
    }

    private CandidatureResponse toResponse(Candidature c) {
        return cvRepository.findByCandidatureId(c.getId())
                .map(cv -> new CandidatureResponse(c, cv.getFichierUrl(), cv.getScoreMatching()))
                .orElseGet(() -> new CandidatureResponse(c, null, null));
    }
}