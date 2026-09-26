package com.recrutementia.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Sous-type d'Utilisateur : partage le meme id (authentification unifiee)
@Entity
@Table(name = "candidat")
@Getter
@Setter
public class Candidat {

    @Id
    private Long id; // meme valeur que utilisateur.id

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Utilisateur utilisateur;

    private String telephone;
}
