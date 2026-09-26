package com.recrutementia.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// VERSION SIMULEE (mock) : pas d'appel reseau reel a OpenAI.
// Utile pour faire fonctionner et demontrer le projet sans cle API payante.
@Service
public class OpenAiCvAnalyseService {

    private static final List<String> COMPETENCES_CONNUES = List.of(
            "Java", "Spring Boot", "Spring", "SQL", "MySQL", "PostgreSQL",
            "JavaScript", "TypeScript", "Angular", "React", "Vue",
            "Python", "Django", "Flask", "C++", "C#", ".NET",
            "HTML", "CSS", "Git", "Docker", "Kubernetes", "AWS", "Azure",
            "Node.js", "PHP", "Laravel", "Symfony", "MongoDB", "REST", "API"
    );

    public CvAnalyseResultat analyserCv(String texteCv) {
        String texteMinuscule = texteCv.toLowerCase();

        List<String> competencesTrouvees = new ArrayList<>();
        for (String competence : COMPETENCES_CONNUES) {
            if (texteMinuscule.contains(competence.toLowerCase())) {
                competencesTrouvees.add(competence);
            }
        }

        CvAnalyseResultat resultat = new CvAnalyseResultat();
        resultat.setCompetences(competencesTrouvees);
        resultat.setExperience("Analyse simulee : " + competencesTrouvees.size()
                + " competence(s) technique(s) detectee(s) dans le document.");
        resultat.setResume("Ceci est une analyse simulee (mode demo, sans appel a une IA externe). "
                + "Competences detectees par mots-cles : "
                + String.join(", ", competencesTrouvees));

        return resultat;
    }
}