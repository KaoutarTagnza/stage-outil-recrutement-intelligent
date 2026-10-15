
package com.recrutementia.service;

import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Analyse simulee du CV par mots-cles.
 * Aucun appel a une API d'intelligence artificielle externe.
 */
@Service
public class OpenAiCvAnalyseService {

    private static final List<String> COMPETENCES_CONNUES = List.of(
            "Java", "Spring Boot", "Spring", "SQL", "MySQL",
            "PostgreSQL", "JavaScript", "TypeScript", "Angular",
            "React", "Vue", "Python", "Django", "Flask",
            "C++", "C#", ".NET", "HTML", "CSS", "Git",
            "Docker", "Kubernetes", "AWS", "Azure", "Node.js",
            "PHP", "Laravel", "Symfony", "MongoDB", "REST API"
    );

    /**
     * Alias reconnus pour certaines competences.
     * Chaque cle represente le nom canonique d'une competence.
     */
    private static final Map<String, List<String>> ALIAS_COMPETENCES = Map.of(
            "javascript", List.of("javascript", "js"),
            "postgresql", List.of("postgresql", "postgres"),
            "rest api", List.of("rest api", "api rest"),
            "node.js", List.of("node.js", "nodejs"),
            "c++", List.of("c++"),
            "c#", List.of("c#", "c sharp"),
            "spring boot", List.of("spring boot"),
            "mysql", List.of("mysql", "my sql"),
            "typescript", List.of("typescript", "ts")
    );

    /**
     * Analyse le texte extrait du CV et detecte les competences connues.
     */
    public CvAnalyseResultat analyserCv(String texteCv) {

        if (texteCv == null || texteCv.isBlank()) {
            throw new IllegalStateException(
                    "Aucun texte n'a pu etre extrait du PDF. "
                    + "Utilisez un PDF contenant du texte selectionnable, "
                    + "pas uniquement une image scannee."
            );
        }

        Set<String> competencesTrouvees = new LinkedHashSet<>();

        for (String competence : COMPETENCES_CONNUES) {
            if (contientCompetence(texteCv, competence)) {
                competencesTrouvees.add(competence);
            }
        }

        CvAnalyseResultat resultat = new CvAnalyseResultat();

        resultat.setCompetences(new ArrayList<>(competencesTrouvees));

        resultat.setExperience(
                "Analyse simulee : "
                + competencesTrouvees.size()
                + " competence(s) technique(s) detectee(s) dans le document."
        );

        resultat.setResume(
                "Analyse simulee (sans appel a une IA externe). "
                + "Competences detectees par mots-cles : "
                + (competencesTrouvees.isEmpty()
                ? "aucune competence connue detectee"
                : String.join(", ", competencesTrouvees))
        );

        return resultat;
    }

    /**
     * Verifie si une competence apparait dans un texte.
     * Les alias techniques connus sont egalement pris en compte.
     */
    static boolean contientCompetence(String texte, String competence) {

        if (texte == null || texte.isBlank()
                || competence == null || competence.isBlank()) {
            return false;
        }

        String texteNormalise = normaliser(texte);
        String competenceNormalisee = normaliser(competence);

        // Recherche directe.
        if (correspond(texteNormalise, competenceNormalisee)) {
            return true;
        }

        // Recherche des alias de la competence demandee.
        for (Map.Entry<String, List<String>> entree
                : ALIAS_COMPETENCES.entrySet()) {

            String nomCanonique = entree.getKey();
            List<String> alias = entree.getValue();

            boolean competenceCorrespond =
                    competenceNormalisee.equals(nomCanonique)
                    || alias.stream().anyMatch(
                            a -> normaliser(a).equals(competenceNormalisee)
                    );

            if (competenceCorrespond) {
                for (String variante : alias) {
                    if (correspond(
                            texteNormalise,
                            normaliser(variante))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Uniformise la casse et les accents.
     */
    private static String normaliser(String texte) {
        return Normalizer.normalize(
                        texte,
                        Normalizer.Form.NFD
                )
                .replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT)
                .trim();
    }

    /**
     * Recherche un terme complet pour eviter les correspondances
     * incorrectes, par exemple Java dans JavaScript.
     */
    private static boolean correspond(String texte, String terme) {

        if (terme == null || terme.isBlank()) {
            return false;
        }

        String expression = Arrays.stream(terme.split("\\s+"))
                .map(Pattern::quote)
                .collect(Collectors.joining("\\s+"));

        String regex = "(?<![\\p{L}\\p{N}])"
                + expression
                + "(?![\\p{L}\\p{N}])";

        return Pattern.compile(regex)
                .matcher(texte)
                .find();
    }
}
