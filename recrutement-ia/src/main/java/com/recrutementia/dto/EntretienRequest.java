package com.recrutementia.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EntretienRequest {

    @NotNull
    private LocalDateTime dateEntretien;
}