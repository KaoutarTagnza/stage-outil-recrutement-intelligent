package com.recrutementia.service;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CvAnalyseResultat {
    private List<String> competences;
    private String experience;
    private String resume;
}