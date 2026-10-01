Bien sûr. Voici le **guide final**, prêt à copier-coller dans ton `README.md`. J’ai repris le fonctionnement réel que tu m’as donné, notamment le script SQL séparé, l’analyse IA simulée et le scénario complet de test.

````markdown
# Plateforme de recrutement intelligente — IA & Cybersécurité

## Guide de démarrage

Ce document explique comment installer, configurer et lancer la plateforme de recrutement intelligente en local.

Le projet est composé de deux parties :

- **Backend** : API REST développée avec Spring Boot et Java
- **Frontend** : interface web développée avec Angular
- **Base de données** : MySQL

---

# 1. Structure du projet

```text
stage-outil-recrutement-intelligent/
│
├── recrutement-ia/                    → Backend Spring Boot
│
├── recrutement-ia-frontend/           → Frontend Angular
│
├── script_mysql_recrutement_ia.sql    → Script de création de la BDD
│
└── README.md                           → Guide de démarrage
````

---

# 2. Prérequis

Avant de lancer le projet, installer les outils suivants :

| Outil       | Version                  | Vérification      |
| ----------- | ------------------------ | ----------------- |
| Java JDK    | 21                       | `java -version`   |
| Maven       | Compatible Spring Boot 3 | `mvn -version`    |
| MySQL       | 8.0 ou supérieur         | `mysql --version` |
| Node.js     | 18 ou supérieur          | `node -v`         |
| Angular CLI | 18 ou supérieur          | `ng version`      |

Un IDE tel que **IntelliJ IDEA**, **VS Code** ou **Eclipse** peut être utilisé pour le développement.

---

# 3. Créer la base de données MySQL

Le projet contient le fichier :

```text
script_mysql_recrutement_ia.sql
```

Ce fichier se trouve à la racine du projet.

## Étapes

### Étape 1

Ouvrir **MySQL Workbench** ou un autre client MySQL.

### Étape 2

Ouvrir le fichier :

```text
script_mysql_recrutement_ia.sql
```

### Étape 3

Exécuter l'intégralité du script SQL.

Le script crée automatiquement la base de données :

```text
recrutement_ia
```

ainsi que les tables nécessaires au fonctionnement de la plateforme.

## Tables principales

Le script crée notamment les 12 tables suivantes :

1. `utilisateur`
2. `candidat`
3. `competence`
4. `offre`
5. `offre_competence`
6. `candidat_competence`
7. `candidature`
8. `cv`
9. `entretien`
10. `evaluation`
11. `notification`
12. `log_action`

Le script contient également :

* les clés primaires ;
* les clés étrangères ;
* les contraintes d'unicité ;
* les relations entre les tables ;
* les index nécessaires aux performances.

---

# 4. Configuration du backend

Le backend se trouve dans :

```text
recrutement-ia/
```

Ouvrir ce dossier avec IntelliJ IDEA, VS Code ou Eclipse.

Le fichier de configuration se trouve ici :

```text
recrutement-ia/src/main/resources/application.properties
```

## Configuration MySQL

Vérifier les paramètres de connexion à la base de données.

Exemple :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/recrutement_ia
spring.datasource.username=root
spring.datasource.password=CHANGE_MOI
```

Remplacer :

```text
CHANGE_MOI
```

par le mot de passe MySQL utilisé sur la machine.

**Ne pas publier de mot de passe réel, de clé API ou de donnée sensible sur GitHub.**

---

# 5. Lancer le backend

Depuis le dossier :

```text
recrutement-ia/
```

le backend peut être lancé avec Maven :

```bash
mvn spring-boot:run
```

Il est également possible de lancer directement la classe principale :

```text
RecrutementIaApplication.java
```

Une fois le lancement terminé, le backend écoute sur :

```text
http://localhost:8080
```

Dans les logs, vérifier notamment que l'application Spring Boot a démarré correctement.

---

# 6. Analyse IA des CV

L'analyse des CV est actuellement disponible en **version simulée**.

Le service :

```text
OpenAiCvAnalyseService.java
```

analyse le contenu du CV à partir de mots-clés techniques afin de calculer les informations utilisées pour le matching.

Cette version ne nécessite pas de clé API OpenAI payante.

Le projet est cependant structuré de manière à pouvoir être connecté ultérieurement à une véritable API d'intelligence artificielle.

---

# 7. Lancer le frontend Angular

Le frontend se trouve dans :

```text
recrutement-ia-frontend/
```

Ouvrir un terminal dans ce dossier.

## Installer les dépendances

La première fois :

```bash
npm install
```

## Lancer Angular

```bash
ng serve
```

L'application sera accessible à l'adresse :

```text
http://localhost:4200
```

Le backend doit être lancé en parallèle afin que le frontend puisse communiquer avec l'API REST :

```text
Frontend Angular
       ↓
http://localhost:4200
       ↓
API REST Spring Boot
       ↓
http://localhost:8080
       ↓
Base de données MySQL
```

---

# 8. Tester l'application

Aucun compte n'est créé automatiquement.

Les comptes peuvent être créés depuis la page d'inscription :

```text
http://localhost:4200/register
```

Les rôles disponibles lors de l'inscription sont :

* Candidat
* Recruteur

---

# 9. Tester avec un compte Recruteur

Créer un compte avec le rôle :

```text
RECRUTEUR
```

Puis se connecter.

Le recruteur peut notamment :

1. créer une offre ;
2. publier l'offre ;
3. consulter les candidatures ;
4. consulter les CV déposés ;
5. lancer l'analyse du CV ;
6. consulter le score de matching ;
7. générer les questions d'entretien ;
8. planifier un entretien ;
9. consulter l'évaluation ;
10. prendre la décision finale concernant la candidature.

---

# 10. Tester avec un compte Candidat

Créer un compte avec le rôle :

```text
CANDIDAT
```

Puis se connecter.

Le candidat peut notamment :

1. consulter les offres disponibles ;
2. sélectionner une offre ;
3. déposer une candidature ;
4. déposer son CV au format PDF ;
5. consulter le suivi de sa candidature ;
6. consulter les informations liées à sa candidature.

Le système analyse ensuite le CV et calcule un score de matching avec l'offre.

---

# 11. Compte Administrateur

L'inscription publique ne propose pas directement le rôle Administrateur.

Pour créer un compte administrateur, il est possible d'utiliser directement l'API.

Exemple :

```text
POST http://localhost:8080/api/auth/register
```

avec un corps JSON similaire à :

```json
{
  "nom": "Admin",
  "email": "admin@test.com",
  "motDePasse": "motdepasse123",
  "role": "ADMIN",
  "consentementDonnees": true
}
```

Le compte administrateur permet notamment d'accéder aux fonctionnalités d'administration, aux statistiques et au journal des actions.

---

# 12. Scénario de test complet

Le scénario suivant permet de tester le fonctionnement global de la plateforme.

## Étape 1 — Recruteur

Se connecter avec un compte Recruteur.

Créer une nouvelle offre puis la publier.

---

## Étape 2 — Candidat

Se déconnecter du compte Recruteur.

Se connecter avec un compte Candidat.

Consulter les offres disponibles.

Sélectionner l'offre créée précédemment.

Déposer une candidature.

Déposer un CV au format PDF.

---

## Étape 3 — Analyse du CV

Le système analyse le CV.

Les compétences et informations pertinentes sont extraites.

Un score de matching est calculé entre le profil du candidat et l'offre.

Le score est utilisé comme **aide à la décision**.

---

## Étape 4 — Entretien

Se reconnecter avec le compte Recruteur.

Consulter les candidatures reçues.

Générer les questions d'entretien.

Planifier l'entretien.

---

## Étape 5 — Évaluation

Après l'entretien, le recruteur peut renseigner :

* une note globale ;
* un commentaire ;
* une décision d'évaluation.

---

## Étape 6 — Décision finale

Le recruteur prend la décision finale concernant la candidature.

Une justification est obligatoire en cas d'acceptation ou de rejet selon les règles fonctionnelles de l'application.

La décision finale reste sous la responsabilité du recruteur.

L'IA fournit des recommandations et des scores, mais ne prend pas automatiquement la décision finale de recrutement.

---

## Étape 7 — Administration

Se connecter avec le compte Administrateur.

Accéder à l'espace :

```text
/admin
```

L'administrateur peut notamment consulter :

* les statistiques ;
* les comptes ;
* les informations liées aux rôles ;
* le journal des actions.

---

# 13. Fonctionnalités principales

## Candidat

* Création de compte
* Authentification
* Consultation des offres
* Dépôt de candidature
* Dépôt de CV
* Suivi des candidatures
* Notifications

## Recruteur

* Authentification
* Création d'offres
* Publication d'offres
* Consultation des candidatures
* Analyse des CV
* Calcul du matching
* Génération de questions d'entretien
* Planification d'entretiens
* Évaluation des candidats
* Décision finale avec justification

## Administrateur

* Gestion des utilisateurs
* Gestion des rôles
* Consultation des statistiques
* Consultation du journal des actions

---

# 14. Sécurité

La plateforme intègre plusieurs mécanismes de sécurité.

## Authentification

L'authentification utilise :

* Spring Security
* JWT
* gestion des rôles

Les rôles principaux sont :

```text
ADMIN
RECRUTEUR
CANDIDAT
```

## Protection des mots de passe

Les mots de passe sont protégés à l'aide de **BCrypt**.

## Journalisation

Les actions sensibles peuvent être enregistrées dans la table :

```text
log_action
```

Les informations enregistrées comprennent notamment :

* l'action effectuée ;
* les détails ;
* l'adresse IP ;
* la date et l'heure ;
* l'utilisateur concerné.

---

# 15. Protection des données

La plateforme prévoit notamment :

* le consentement explicite lors de l'inscription ;
* la gestion des rôles et des autorisations ;
* la journalisation des actions sensibles ;
* une validation humaine pour la décision finale de recrutement.

Les informations personnelles et les CV doivent être manipulés uniquement dans le cadre prévu par l'application.

---

# 16. Architecture technique

L'architecture générale est la suivante :

```text
                    ┌──────────────────────┐
                    │      Utilisateur     │
                    │ Candidat/Recruteur   │
                    │       /Admin         │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │   Angular Frontend   │
                    │      Port 4200       │
                    └──────────┬───────────┘
                               │
                          REST / JWT
                               │
                               ▼
                    ┌──────────────────────┐
                    │  Spring Boot Backend │
                    │      Port 8080       │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │    MySQL Database    │
                    │   recrutement_ia     │
                    └──────────────────────┘
```

---

# 17. Technologies utilisées

| Composant              | Technologie                       |
| ---------------------- | --------------------------------- |
| Backend                | Spring Boot 3                     |
| Langage                | Java 21                           |
| Sécurité               | Spring Security + JWT             |
| Persistance            | Spring Data JPA / Hibernate       |
| Base de données        | MySQL 8                           |
| Frontend               | Angular                           |
| API                    | REST                              |
| Gestion du projet Java | Maven                             |
| IDE                    | IntelliJ IDEA / VS Code / Eclipse |
| Analyse IA             | Service d'analyse de CV simulé    |

---

# 18. Résolution des problèmes courants

## Le backend ne démarre pas

Vérifier :

* que Java 21 est installé ;
* que MySQL est démarré ;
* que la base `recrutement_ia` existe ;
* que les identifiants MySQL dans `application.properties` sont corrects.

---

## Le frontend ne démarre pas

Vérifier que Node.js est installé :

```bash
node -v
```

Puis installer les dépendances :

```bash
npm install
```

Et relancer :

```bash
ng serve
```

---

## Le frontend ne communique pas avec le backend

Vérifier que les deux serveurs sont démarrés :

```text
Angular  → http://localhost:4200
Spring   → http://localhost:8080
```

Le backend doit être lancé avant d'effectuer les tests fonctionnels nécessitant l'API.

---

# 19. Ordre recommandé pour lancer le projet

Pour une nouvelle installation :

### 1. Installer les prérequis

Java, Maven, MySQL, Node.js et Angular CLI.

### 2. Créer la base de données

Exécuter :

```text
script_mysql_recrutement_ia.sql
```

### 3. Configurer le backend

Modifier si nécessaire :

```text
recrutement-ia/src/main/resources/application.properties
```

### 4. Lancer Spring Boot

```bash
cd recrutement-ia
mvn spring-boot:run
```

### 5. Installer les dépendances Angular

Dans un autre terminal :

```bash
cd recrutement-ia-frontend
npm install
```

### 6. Lancer Angular

```bash
ng serve
```

### 7. Ouvrir l'application

```text
http://localhost:4200
```

---



Et dans `README.md`, tu peux **copier-coller tout le guide ci-dessus**.
