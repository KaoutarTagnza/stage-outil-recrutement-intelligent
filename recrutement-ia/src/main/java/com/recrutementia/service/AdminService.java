package com.recrutementia.service;

import com.recrutementia.dto.*;
import com.recrutementia.model.*;
import com.recrutementia.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UtilisateurRepository utilisateurRepository;
    private final OffreRepository offreRepository;
    private final CandidatureRepository candidatureRepository;
    private final LogActionRepository logActionRepository;
    private final JournalisationService journalisationService;

    public List<UtilisateurResponse> listerUtilisateurs() {
        return utilisateurRepository.findAll().stream()
                .map(UtilisateurResponse::new)
                .collect(Collectors.toList());
    }

    public UtilisateurResponse changerStatut(Long id, boolean actif, String emailAdmin) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable"));

        utilisateur.setActif(actif);
        utilisateurRepository.save(utilisateur);

        Utilisateur admin = utilisateurRepository.findByEmail(emailAdmin)
                .orElseThrow(() -> new IllegalStateException("Admin introuvable"));
        journalisationService.journaliser(admin, "Statut de l'utilisateur #" + id + " change en "
                + (actif ? "actif" : "desactive"));

        return new UtilisateurResponse(utilisateur);
    }

    public UtilisateurResponse changerRole(Long id, Role nouveauRole, String emailAdmin) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable"));

        if (utilisateur.getRole() == Role.ADMIN && nouveauRole != Role.ADMIN) {
            long nbAdmins = utilisateurRepository.findAll().stream()
                    .filter(u -> u.getRole() == Role.ADMIN)
                    .count();
            if (nbAdmins <= 1) {
                throw new IllegalStateException("Impossible de retirer le dernier compte administrateur");
            }
        }

        utilisateur.setRole(nouveauRole);
        utilisateurRepository.save(utilisateur);

        Utilisateur admin = utilisateurRepository.findByEmail(emailAdmin)
                .orElseThrow(() -> new IllegalStateException("Admin introuvable"));
        journalisationService.journaliser(admin, "Role de l'utilisateur #" + id + " change en " + nouveauRole);

        return new UtilisateurResponse(utilisateur);
    }

    public List<LogActionResponse> listerLogs() {
        return logActionRepository.findAll().stream()
                .sorted(Comparator.comparing(LogAction::getDateHeure).reversed())
                .map(LogActionResponse::new)
                .collect(Collectors.toList());
    }

    public StatistiquesResponse obtenirStatistiques() {
        List<Offre> offres = offreRepository.findAll();
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        List<Candidature> candidatures = candidatureRepository.findAll();

        long nbOffresPubliees = offres.stream().filter(o -> o.getStatut() == StatutOffre.PUBLIEE).count();
        long nbCandidats = utilisateurs.stream().filter(u -> u.getRole() == Role.CANDIDAT).count();
        long nbRecruteurs = utilisateurs.stream().filter(u -> u.getRole() == Role.RECRUTEUR).count();
        long nbAcceptees = candidatures.stream().filter(c -> c.getStatut() == StatutCandidature.ACCEPTEE).count();
        long nbRejetees = candidatures.stream().filter(c -> c.getStatut() == StatutCandidature.REJETEE).count();

        long nbDecidees = nbAcceptees + nbRejetees;
        double taux = nbDecidees == 0 ? 0.0 : (nbAcceptees * 100.0) / nbDecidees;

        return new StatistiquesResponse(
                offres.size(), nbOffresPubliees, nbCandidats, nbRecruteurs,
                candidatures.size(), nbAcceptees, nbRejetees, Math.round(taux * 100.0) / 100.0
        );
    }
}