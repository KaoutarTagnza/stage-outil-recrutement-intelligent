package com.recrutementia.service;

import com.recrutementia.dto.OffreRequest;
import com.recrutementia.dto.OffreResponse;
import com.recrutementia.model.*;
import com.recrutementia.repository.CompetenceRepository;
import com.recrutementia.repository.OffreRepository;
import com.recrutementia.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OffreService {

    private final OffreRepository offreRepository;
    private final CompetenceRepository competenceRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final JournalisationService journalisationService;

    public OffreResponse creerOffre(OffreRequest req, String emailRecruteur) {
        Utilisateur recruteur = utilisateurRepository.findByEmail(emailRecruteur)
                .orElseThrow(() -> new IllegalStateException("Recruteur introuvable"));

        Offre offre = new Offre();
        offre.setTitre(req.getTitre());
        offre.setDescription(req.getDescription());
        offre.setDateLimite(req.getDateLimite());
        offre.setRecruteur(recruteur);
        offre.setStatut(StatutOffre.BROUILLON);
        offre.setCompetences(resoudreCompetences(req.getCompetences()));

        offre = offreRepository.save(offre);
        journalisationService.journaliser(recruteur, "Creation de l'offre #" + offre.getId() + " (" + offre.getTitre() + ")");

        return new OffreResponse(offre);
    }

    public OffreResponse publierOffre(Long offreId, String emailRecruteur) {
        Offre offre = offreRepository.findById(offreId)
                .orElseThrow(() -> new IllegalStateException("Offre introuvable"));

        verifierProprietaire(offre, emailRecruteur);

        offre.setStatut(StatutOffre.PUBLIEE);
        offre = offreRepository.save(offre);

        journalisationService.journaliser(offre.getRecruteur(), "Publication de l'offre #" + offre.getId());

        return new OffreResponse(offre);
    }

    public List<OffreResponse> listerOffresPubliees() {
        return offreRepository.findByStatut(StatutOffre.PUBLIEE).stream()
                .map(OffreResponse::new)
                .collect(Collectors.toList());
    }

    public List<OffreResponse> listerMesOffres(String emailRecruteur) {
        Utilisateur recruteur = utilisateurRepository.findByEmail(emailRecruteur)
                .orElseThrow(() -> new IllegalStateException("Recruteur introuvable"));

        return offreRepository.findByRecruteurId(recruteur.getId()).stream()
                .map(OffreResponse::new)
                .collect(Collectors.toList());
    }

    public OffreResponse obtenirOffre(Long id) {
        Offre offre = offreRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Offre introuvable"));
        return new OffreResponse(offre);
    }

    private Set<Competence> resoudreCompetences(List<String> noms) {
        Set<Competence> resultat = new HashSet<>();
        if (noms == null) return resultat;

        for (String nom : noms) {
            Competence competence = competenceRepository.findByNom(nom)
                    .orElseGet(() -> {
                        Competence c = new Competence();
                        c.setNom(nom);
                        return competenceRepository.save(c);
                    });
            resultat.add(competence);
        }
        return resultat;
    }

    private void verifierProprietaire(Offre offre, String emailRecruteur) {
        if (!offre.getRecruteur().getEmail().equals(emailRecruteur)) {
            throw new IllegalStateException("Vous n'etes pas le proprietaire de cette offre");
        }
    }
}