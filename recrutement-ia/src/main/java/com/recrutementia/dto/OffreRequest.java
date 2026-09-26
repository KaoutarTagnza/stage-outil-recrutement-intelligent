package com.recrutementia.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OffreRequest {

    @NotBlank
    private String titre;

    private String description;

    private LocalDateTime dateLimite;

    // Liste de noms de competences, ex: ["Java", "Spring Boot", "SQL"]
    private List<String> competences;
}