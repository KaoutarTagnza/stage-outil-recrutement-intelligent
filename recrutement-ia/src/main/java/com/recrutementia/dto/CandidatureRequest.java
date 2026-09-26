package com.recrutementia.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidatureRequest {

    @NotNull
    private Long offreId;
}