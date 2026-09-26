package com.recrutementia.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "entretien")
@Getter
@Setter
public class Entretien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_entretien", nullable = false)
    private LocalDateTime dateEntretien;

    @Column(name = "questions_generees", columnDefinition = "TEXT")
    private String questionsGenerees;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToOne
    @JoinColumn(name = "candidature_id", nullable = false, unique = true)
    private Candidature candidature;
}
