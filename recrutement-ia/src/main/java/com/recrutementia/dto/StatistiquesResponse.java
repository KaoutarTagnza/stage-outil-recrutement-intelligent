package com.recrutementia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatistiquesResponse {
    private long nbOffresTotal;
    private long nbOffresPubliees;
    private long nbCandidats;
    private long nbRecruteurs;
    private long nbCandidaturesTotal;
    private long nbCandidaturesAcceptees;
    private long nbCandidaturesRejetees;
    private double tauxAcceptationPourcent;
}