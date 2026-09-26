package com.recrutementia.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// VERSION SIMULEE : genere des questions a partir des competences de l'offre,
// sans appel a une IA externe (meme principe que OpenAiCvAnalyseService).
@Service
public class QuestionGeneratorService {

    public List<String> genererQuestions(String titreOffre, List<String> competencesRequises) {
        List<String> questions = new ArrayList<>();

        questions.add("Pouvez-vous vous presenter et expliquer votre parcours en lien avec le poste de "
                + titreOffre + " ?");

        for (String competence : competencesRequises) {
            questions.add("Pouvez-vous decrire une experience concrete ou vous avez utilise " + competence + " ?");
        }

        questions.add("Quelles sont vos plus grandes forces et un axe d'amelioration ?");
        questions.add("Pourquoi souhaitez-vous rejoindre ce poste ?");

        return questions;
    }
}