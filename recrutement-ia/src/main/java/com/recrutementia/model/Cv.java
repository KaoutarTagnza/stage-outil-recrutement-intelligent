package com.recrutementia.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cv")
@Getter
@Setter
public class Cv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fichier_url", nullable = false)
    private String fichierUrl;

    @Column(name = "competences_extraites", columnDefinition = "TEXT")
    private String competencesExtraites;

    @Column(name = "experience_extraite", columnDefinition = "TEXT")
    private String experienceExtraite;

    @Column(name = "resume_ia", columnDefinition = "TEXT")
    private String resumeIa;

    @Column(name = "score_matching", precision = 5, scale = 2)
    private BigDecimal scoreMatching = BigDecimal.ZERO;

    @OneToOne
    @JoinColumn(name = "candidature_id", nullable = false, unique = true)
    private Candidature candidature;
}
