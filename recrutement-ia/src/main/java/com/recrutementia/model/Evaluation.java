package com.recrutementia.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "evaluation")
@Getter
@Setter
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "note_globale", precision = 4, scale = 2)
    private BigDecimal noteGlobale;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    @Enumerated(EnumType.STRING)
    private DecisionEvaluation decision;

    @OneToOne
    @JoinColumn(name = "entretien_id", nullable = false, unique = true)
    private Entretien entretien;

    @ManyToOne
    @JoinColumn(name = "recruteur_id", nullable = false)
    private Utilisateur recruteur;
}
