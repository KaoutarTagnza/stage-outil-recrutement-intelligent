package com.recrutementia.dto;

import com.recrutementia.model.Entretien;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EntretienResponse {

    private final Long id;
    private final LocalDateTime dateEntretien;
    private final String questionsGenerees;
    private final String notes;
    private final Long candidatureId;
    private final String candidatNom;
    private final String offreTitre;

    public EntretienResponse(Entretien e) {
        this.id = e.getId();
        this.dateEntretien = e.getDateEntretien();
        this.questionsGenerees = e.getQuestionsGenerees();
        this.notes = e.getNotes();
        this.candidatureId = e.getCandidature().getId();
        this.candidatNom = e.getCandidature().getCandidat().getUtilisateur().getNom();
        this.offreTitre = e.getCandidature().getOffre().getTitre();
    }
}