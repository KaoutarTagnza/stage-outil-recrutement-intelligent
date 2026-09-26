package com.recrutementia.dto;

import com.recrutementia.model.Utilisateur;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UtilisateurResponse {

    private final Long id;
    private final String nom;
    private final String email;
    private final String role;
    private final Boolean actif;
    private final LocalDateTime dateCreation;

    public UtilisateurResponse(Utilisateur u) {
        this.id = u.getId();
        this.nom = u.getNom();
        this.email = u.getEmail();
        this.role = u.getRole().name();
        this.actif = u.getActif();
        this.dateCreation = u.getDateCreation();
    }
}