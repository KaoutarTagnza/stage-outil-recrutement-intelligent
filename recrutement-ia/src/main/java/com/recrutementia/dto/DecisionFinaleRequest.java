package com.recrutementia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DecisionFinaleRequest {

    @NotNull
    private Boolean accepter; // true = ACCEPTEE, false = REJETEE

    @NotBlank(message = "La justification est obligatoire pour toute decision finale")
    private String justification;
}