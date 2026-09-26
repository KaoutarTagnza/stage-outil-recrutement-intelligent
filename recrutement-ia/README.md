# Recrutement IA — Backend Spring Boot

Projet de stage — plateforme de recrutement intelligente (Angular + Spring Boot + MySQL + API IA).

## Ce qui est déjà fait dans ce squelette

- `pom.xml` — Spring Boot 3.3, Java 21, Spring Web, Spring Data JPA, Spring Security, MySQL, JWT (jjwt), Lombok
- `application.properties` — configuration base de données + JWT
- **10 entités JPA** dans `model/` correspondant exactement au script SQL (`Utilisateur`, `Candidat`, `Competence`, `Offre`, `Candidature`, `Cv`, `Entretien`, `Evaluation`, `Notification`, `LogAction`)
- **9 repositories** Spring Data JPA dans `repository/`
- **Sécurité JWT complète** : `JwtUtil`, `JwtAuthFilter`, `UserDetailsServiceImpl`, `SecurityConfig`
- **Authentification fonctionnelle** : `POST /api/auth/register` et `POST /api/auth/login`, avec journalisation automatique dans `LogAction` (traçabilité)
- Gestion des erreurs centralisée (`GlobalExceptionHandler`)

## Avant de lancer le projet

1. Créer la base de données avec le script SQL fourni précédemment (`script_mysql_recrutement_ia.sql`) :
   ```
   mysql -u root -p < script_mysql_recrutement_ia.sql
   ```
2. Modifier `src/main/resources/application.properties` avec ton mot de passe MySQL local.
3. **Important** : change la valeur de `jwt.secret` avant toute mise en production (ici c'est une valeur de démo).

## Lancer le projet

```bash
mvn spring-boot:run
```

Le serveur démarre sur `http://localhost:8080`.

## Tester l'authentification

**Inscription d'un candidat :**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"nom":"Sara Amrani","email":"sara@test.com","motDePasse":"motdepasse123","role":"CANDIDAT","telephone":"0600000000"}'
```

**Connexion :**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"sara@test.com","motDePasse":"motdepasse123"}'
```

Tu recevras un token JWT à utiliser dans le header `Authorization: Bearer <token>` pour les prochaines requêtes.

## Prochaines étapes (à développer)

Dans l'ordre logique pour avancer :

1. `OffreController` + `OffreService` — CRUD des offres (créer/publier/lister)
2. `CandidatureController` + `CandidatureService` — postuler, déposer un CV
3. `CvAnalyseService` — appel à l'API OpenAI pour extraire compétences + calculer le score de matching
4. `EntretienController` — génération de questions IA + planification
5. `EvaluationController` — décision finale avec justification obligatoire (déjà prévu dans le modèle `Candidature.justification`)
6. `AdminController` — gestion des comptes, des rôles, consultation des logs et statistiques

## Structure des packages

```
com.recrutementia
├── config          → SecurityConfig (règles d'accès par rôle, CORS)
├── security        → JwtUtil, JwtAuthFilter, UserDetailsServiceImpl
├── model           → Entités JPA (correspondent aux tables MySQL)
├── repository      → Interfaces Spring Data JPA
├── service         → Logique métier (AuthService fait, le reste à ajouter)
├── controller      → Endpoints REST (AuthController fait, le reste à ajouter)
├── dto             → Objets d'échange avec Angular
└── exception       → Gestion centralisée des erreurs
```
