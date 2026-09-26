package com.recrutementia.dto;

import com.recrutementia.model.LogAction;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LogActionResponse {

    private final Long id;
    private final String action;
    private final String details;
    private final String adresseIp;
    private final LocalDateTime dateHeure;
    private final String utilisateurNom;

    public LogActionResponse(LogAction log) {
        this.id = log.getId();
        this.action = log.getAction();
        this.details = log.getDetails();
        this.adresseIp = log.getAdresseIp();
        this.dateHeure = log.getDateHeure();
        this.utilisateurNom = log.getUtilisateur().getNom();
    }
}