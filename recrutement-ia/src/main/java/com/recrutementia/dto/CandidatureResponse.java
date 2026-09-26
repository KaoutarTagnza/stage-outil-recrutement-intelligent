package com.recrutementia.dto;

import com.recrutementia.model.Candidature;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class CandidatureResponse {

    private final Long id;
    private final String statut;
    private final LocalDateTime dateDepot;
    private final String offreTitre;
    private final Long offreId;
    private final String candidatNom;
    private final String cvFichierUrl;
    private final BigDecimal scoreMatching;

    public CandidatureResponse(Candidature c, String cvFichierUrl, BigDecimal scoreMatching) {
        this.id = c.getId();
        this.statut = c.getStatut().name();
        this.dateDepot = c.getDateDepot();
        this.offreTitre = c.getOffre().getTitre();
        this.offreId = c.getOffre().getId();
        this.candidatNom = c.getCandidat().getUtilisateur().getNom();
        this.cvFichierUrl = cvFichierUrl;
        this.scoreMatching = scoreMatching;
    }
}