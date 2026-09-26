package com.recrutementia.dto;

import com.recrutementia.model.Evaluation;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class EvaluationResponse {

    private final Long id;
    private final BigDecimal noteGlobale;
    private final String commentaire;
    private final String decision;
    private final String recruteurNom;
    private final Long entretienId;

    public EvaluationResponse(Evaluation e) {
        this.id = e.getId();
        this.noteGlobale = e.getNoteGlobale();
        this.commentaire = e.getCommentaire();
        this.decision = e.getDecision().name();
        this.recruteurNom = e.getRecruteur().getNom();
        this.entretienId = e.getEntretien().getId();
    }
}