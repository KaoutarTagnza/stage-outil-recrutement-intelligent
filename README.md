# Guide de démarrage — Plateforme de recrutement intelligente

Ce document explique comment installer et lancer le projet en local (backend + frontend), étape par étape.

## Structure du projet

```
stage-outil-recrutement-intelligent/
├── recrutement-ia/              → Backend (Spring Boot / Java)
└── recrutement-ia-frontend/     → Frontend (Angular)
```

---

## Prérequis à installer

| Outil | Version | Vérification |
|---|---|---|
| Java (JDK) | 21 | `java -version` |
| Maven | fourni avec la plupart des IDE, sinon à installer | `mvn -version` |
| MySQL | 8.0 ou supérieur | via MySQL Workbench |
| Node.js | 18+ | `node -v` |
| Angular CLI | 18+ | `ng version` |

---

## 1. Créer la base de données MySQL

1. Ouvrir MySQL Workbench (ou un client MySQL équivalent)
2. Créer un nouvel onglet SQL et exécuter le script `script_mysql_recrutement_ia.sql` fourni séparément (ou joint au rapport)
3. Vérifier que la base `recrutement_ia` apparaît avec ses 12 tables

---

## 2. Lancer le backend (Spring Boot)

1. Ouvrir le dossier `recrutement-ia/` dans un IDE (IntelliJ, VS Code avec extension Java, ou Eclipse)
2. Ouvrir le fichier `src/main/resources/application.properties`
3. Remplacer la ligne suivante par le mot de passe MySQL réel de la machine :
   ```
   spring.datasource.password=CHANGE_MOI
   ```
4. Lancer la classe principale `RecrutementIaApplication.java` (clic droit → Run, ou `mvn spring-boot:run` dans un terminal)
5. Vérifier dans les logs la ligne : `Started RecrutementIaApplication` (le serveur écoute sur le port **8080**)

### Note sur l'analyse IA

L'analyse de CV est actuellement en **version simulée** (`OpenAiCvAnalyseService.java`) : elle détecte des mots-clés techniques dans le texte du CV plutôt que d'appeler une vraie API IA payante. Le code est prêt à être branché sur l'API OpenAI (voir les commentaires dans le fichier) si une clé API est fournie.

---

## 3. Lancer le frontend (Angular)

1. Ouvrir un terminal dans le dossier `recrutement-ia-frontend/`
2. Installer les dépendances (à faire une seule fois) :
   ```
   npm install
   ```
3. Lancer le serveur de développement :
   ```
   ng serve
   ```
4. Ouvrir un navigateur sur : **http://localhost:4200**

**Important** : le backend (étape 2) doit être lancé et actif pour que le frontend fonctionne — le frontend appelle l'API sur `http://localhost:8080`.

---

## 4. Tester l'application

Aucun compte n'existe par défaut : il faut en créer via la page d'inscription (`/register`).

### Créer un compte recruteur (pour tester la création d'offres)
- Aller sur `http://localhost:4200/register`
- Remplir le formulaire avec le rôle **Recruteur**

### Créer un compte candidat (pour tester la candidature)
- Même page, avec le rôle **Candidat**

### Créer un compte administrateur
- L'inscription publique ne propose que Candidat/Recruteur. Pour créer un compte Admin, envoyer une requête directement à l'API (avec un outil comme Postman ou PowerShell) :
  ```
  POST http://localhost:8080/api/auth/register
  Body JSON : { "nom": "Admin", "email": "admin@test.com", "motDePasse": "motdepasse123", "role": "ADMIN", "consentementDonnees": true }
  ```

### Scénario de test complet
1. Se connecter en **Recruteur** → créer une offre → la publier (`/mes-offres`)
2. Se déconnecter, se connecter en **Candidat** → consulter l'offre (`/offres`) → postuler → déposer un CV en PDF (`/mes-candidatures`) → observer le score de matching calculé automatiquement
3. Se reconnecter en **Recruteur** → consulter les candidatures de l'offre → générer les questions d'entretien → planifier l'entretien → évaluer → prendre la décision finale (acceptation/rejet avec justification obligatoire)
4. Se connecter en **Admin** → consulter les statistiques et le journal des actions (`/admin`)

---

## Résumé technique

| Composant | Technologie |
|---|---|
| Backend | Spring Boot 3, Java 21, Spring Security + JWT |
| Base de données | MySQL 8 |
| Frontend | Angular (standalone components, signals) |
| Sécurité | Authentification par rôle (Admin/Recruteur/Candidat), mots de passe chiffrés (BCrypt), en-têtes de sécurité HTTP, journalisation de toutes les actions sensibles (IP + user-agent) |
| Conformité | Consentement RGPD explicite à l'inscription, validation humaine obligatoire avant toute décision de recrutement |
