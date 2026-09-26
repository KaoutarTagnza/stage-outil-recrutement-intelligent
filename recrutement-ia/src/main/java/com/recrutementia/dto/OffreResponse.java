package com.recrutementia.dto;

import com.recrutementia.model.Offre;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OffreResponse {

    private final Long id;
    private final String titre;
    private final String description;
    private final LocalDateTime dateCreation;
    private final LocalDateTime dateLimite;
    private final String statut;
    private final String recruteurNom;
    private final List<String> competences;

    public OffreResponse(Offre offre) {
        this.id = offre.getId();
        this.titre = offre.getTitre();
        this.description = offre.getDescription();
        this.dateCreation = offre.getDateCreation();
        this.dateLimite = offre.getDateLimite();
        this.statut = offre.getStatut().name();
        this.recruteurNom = offre.getRecruteur().getNom();
        this.competences = offre.getCompetences().stream()
                .map(c -> c.getNom())
                .collect(Collectors.toList());
    }
}