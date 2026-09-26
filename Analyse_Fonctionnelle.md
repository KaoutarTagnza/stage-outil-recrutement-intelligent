# Analyse fonctionnelle
## Plateforme de recrutement intelligente

Ce document détaille chacun des 19 cas d'utilisation identifiés dans le diagramme de cas d'utilisation, avec : acteur, préconditions, scénario nominal, scénario(s) alternatif(s) et postcondition.

---

## A. Cas d'utilisation — Candidat

### UC1 — Se connecter

| Élément | Détail |
|---|---|
| **Acteur** | Candidat |
| **Précondition** | Le candidat possède un compte existant |
| **Scénario nominal** | 1. Le candidat accède à la page de connexion.<br>2. Il saisit son email et son mot de passe.<br>3. Le système vérifie les identifiants.<br>4. Le système génère un jeton JWT et redirige vers l'espace candidat. |
| **Scénario alternatif** | A1 : Identifiants incorrects → message d'erreur, retour à l'étape 2.<br>A2 : Compte non activé → message invitant à vérifier l'email de confirmation. |
| **Postcondition** | Le candidat est authentifié et accède à son espace personnel. |

---

### UC2 — Créer un compte

| Élément | Détail |
|---|---|
| **Acteur** | Candidat |
| **Précondition** | Le candidat n'a pas encore de compte |
| **Scénario nominal** | 1. Le candidat accède au formulaire d'inscription.<br>2. Il saisit nom, email, téléphone, mot de passe.<br>3. Le système vérifie que l'email n'est pas déjà utilisé.<br>4. Le système crée le compte (statut "actif").<br>5. Un email de bienvenue est envoyé. |
| **Scénario alternatif** | A1 : Email déjà existant → message d'erreur, retour à l'étape 2.<br>A2 : Mot de passe trop faible → message d'exigence de sécurité, retour à l'étape 2. |
| **Postcondition** | Un nouveau compte Candidat est créé en base de données. |

---

### UC3 — Consulter les offres

| Élément | Détail |
|---|---|
| **Acteur** | Candidat |
| **Précondition** | Aucune (accessible avec ou sans connexion) |
| **Scénario nominal** | 1. Le candidat accède à la liste des offres.<br>2. Le système affiche les offres au statut "publiée".<br>3. Le candidat peut filtrer par mot-clé ou compétence.<br>4. Le candidat consulte le détail d'une offre. |
| **Scénario alternatif** | A1 : Aucune offre ne correspond au filtre → message "Aucun résultat". |
| **Postcondition** | Le candidat a une vision claire des offres disponibles. |

---

### UC4 — Postuler à une offre

| Élément | Détail |
|---|---|
| **Acteur** | Candidat (connecté) |
| **Précondition** | Le candidat est connecté, l'offre existe et est publiée |
| **Scénario nominal** | 1. Le candidat consulte une offre.<br>2. Il clique sur "Postuler".<br>3. Le système crée une candidature avec le statut "En attente".<br>4. Le candidat est redirigé vers le dépôt de CV (UC5). |
| **Scénario alternatif** | A1 : Le candidat a déjà postulé à cette offre → message "Vous avez déjà postulé". |
| **Postcondition** | Une candidature est créée et liée au candidat et à l'offre. |

---

### UC5 — Déposer un CV

| Élément | Détail |
|---|---|
| **Acteur** | Candidat |
| **Précondition** | Une candidature a été initiée (UC4) |
| **Scénario nominal** | 1. Le candidat sélectionne un fichier CV (PDF).<br>2. Le système vérifie le format et la taille du fichier.<br>3. Le fichier est stocké et une entité CV est créée, liée à la candidature.<br>4. Le système déclenche l'analyse automatique du CV (UC10). |
| **Scénario alternatif** | A1 : Format invalide (≠ PDF) → message d'erreur, retour à l'étape 1.<br>A2 : Fichier trop volumineux → message d'erreur, retour à l'étape 1. |
| **Postcondition** | Le CV est enregistré et associé à la candidature. |

---

### UC6 — Suivre sa candidature

| Élément | Détail |
|---|---|
| **Acteur** | Candidat |
| **Précondition** | Le candidat a au moins une candidature |
| **Scénario nominal** | 1. Le candidat accède à "Mes candidatures".<br>2. Le système affiche la liste avec le statut de chacune (En attente, En entretien, Acceptée, Rejetée).<br>3. Le candidat consulte le détail d'une candidature. |
| **Scénario alternatif** | A1 : Aucune candidature → message "Vous n'avez postulé à aucune offre". |
| **Postcondition** | Le candidat connaît l'état d'avancement de ses candidatures. |

---

## B. Cas d'utilisation — Recruteur

### UC7 — Se connecter

| Élément | Détail |
|---|---|
| **Acteur** | Recruteur |
| **Précondition** | Le recruteur possède un compte créé par un administrateur |
| **Scénario nominal** | 1. Le recruteur saisit ses identifiants.<br>2. Le système vérifie le rôle "Recruteur".<br>3. Le système génère un jeton JWT et redirige vers le tableau de bord. |
| **Scénario alternatif** | A1 : Identifiants incorrects → message d'erreur. |
| **Postcondition** | Le recruteur accède à son espace avec les droits liés à son rôle. |

---

### UC8 — Créer une offre

| Élément | Détail |
|---|---|
| **Acteur** | Recruteur |
| **Précondition** | Le recruteur est connecté |
| **Scénario nominal** | 1. Le recruteur remplit le formulaire (titre, description, compétences requises).<br>2. Il associe une ou plusieurs entités Compétence à l'offre.<br>3. Le système enregistre l'offre avec le statut "publiée". |
| **Scénario alternatif** | A1 : Champs obligatoires manquants → message d'erreur, retour à l'étape 1.<br>A2 : Le recruteur enregistre en "brouillon" au lieu de publier. |
| **Postcondition** | Une nouvelle offre est visible par les candidats (si publiée). |

---

### UC9 — Consulter les candidatures

| Élément | Détail |
|---|---|
| **Acteur** | Recruteur |
| **Précondition** | Le recruteur est connecté et a créé au moins une offre |
| **Scénario nominal** | 1. Le recruteur sélectionne une offre.<br>2. Le système affiche la liste des candidatures reçues.<br>3. Le recruteur consulte le détail d'une candidature (CV, score, statut). |
| **Scénario alternatif** | A1 : Aucune candidature reçue → message "Aucune candidature pour cette offre". |
| **Postcondition** | Le recruteur a une vue complète des candidatures. |

---

### UC10 — Analyser un CV (IA)

| Élément | Détail |
|---|---|
| **Acteur** | Recruteur (déclenchement automatique possible dès UC5) |
| **Précondition** | Un CV a été déposé |
| **Scénario nominal** | 1. Le système envoie le contenu du CV à l'API d'analyse IA.<br>2. L'IA extrait les compétences, l'expérience et la formation.<br>3. Les informations extraites sont enregistrées dans l'entité CV. |
| **Scénario alternatif** | A1 : L'API IA est indisponible → le CV reste "en attente d'analyse", nouvelle tentative automatique.<br>A2 : Le CV est illisible (scan de mauvaise qualité) → analyse partielle signalée au recruteur. |
| **Postcondition** | Le CV dispose de données structurées exploitables pour le matching. |

---

### UC11 — Générer un score de matching

| Élément | Détail |
|---|---|
| **Acteur** | Recruteur (déclenché automatiquement après UC10) |
| **Précondition** | Le CV a été analysé (UC10) et l'offre a des compétences définies |
| **Scénario nominal** | 1. Le système compare les compétences extraites du CV à celles requises par l'offre.<br>2. Un score de compatibilité (0 à 100) est calculé.<br>3. Le score est enregistré dans l'entité CV/Candidature. |
| **Scénario alternatif** | A1 : Aucune compétence en commun → score de 0, la candidature reste visible mais mal classée. |
| **Postcondition** | Chaque candidature dispose d'un score de matching. |

---

### UC12 — Classer les candidats

| Élément | Détail |
|---|---|
| **Acteur** | Recruteur |
| **Précondition** | Plusieurs candidatures ont un score de matching (UC11) |
| **Scénario nominal** | 1. Le recruteur accède à la liste des candidatures d'une offre.<br>2. Le système trie automatiquement les candidats par score décroissant.<br>3. Le recruteur peut re-trier par date ou statut. |
| **Scénario alternatif** | — |
| **Postcondition** | Le recruteur visualise les candidats les mieux classés en priorité. |

---

### UC13 — Générer des questions d'entretien

| Élément | Détail |
|---|---|
| **Acteur** | Recruteur |
| **Précondition** | Une candidature avec CV analysé existe |
| **Scénario nominal** | 1. Le recruteur sélectionne une candidature.<br>2. Il clique sur "Générer des questions".<br>3. Le système envoie le profil du candidat et l'offre à l'API IA.<br>4. L'IA retourne une liste de questions personnalisées.<br>5. Les questions sont affichées et modifiables par le recruteur. |
| **Scénario alternatif** | A1 : L'API IA échoue → un jeu de questions génériques par défaut est proposé. |
| **Postcondition** | Une liste de questions est disponible pour l'entretien. |

---

### UC14 — Planifier un entretien

| Élément | Détail |
|---|---|
| **Acteur** | Recruteur |
| **Précondition** | La candidature est au statut "présélectionnée" |
| **Scénario nominal** | 1. Le recruteur choisit une date et un créneau.<br>2. Le système crée l'entité Entretien liée à la candidature.<br>3. Une notification est envoyée au candidat (UC — Notification).<br>4. Le statut de la candidature passe à "Entretien planifié". |
| **Scénario alternatif** | A1 : Créneau déjà utilisé par le recruteur → message d'erreur, retour à l'étape 1. |
| **Postcondition** | L'entretien est enregistré et le candidat est informé. |

---

### UC15 — Valider ou rejeter

| Élément | Détail |
|---|---|
| **Acteur** | Recruteur |
| **Précondition** | Un entretien a eu lieu et/ou une évaluation existe |
| **Scénario nominal** | 1. Le recruteur consulte le profil et l'évaluation du candidat.<br>2. Il choisit "Accepter" ou "Rejeter".<br>3. Le système impose une justification écrite obligatoire (principe de supervision humaine).<br>4. Le statut de la candidature est mis à jour.<br>5. Une notification est envoyée au candidat. |
| **Scénario alternatif** | A1 : Justification vide → blocage de la validation, retour à l'étape 3. |
| **Postcondition** | La décision finale est enregistrée, avec traçabilité (log) et justification. |

---

## C. Cas d'utilisation — Administrateur

### UC16 — Gérer les comptes

| Élément | Détail |
|---|---|
| **Acteur** | Administrateur |
| **Précondition** | L'administrateur est connecté |
| **Scénario nominal** | 1. L'administrateur consulte la liste des comptes (recruteurs, candidats).<br>2. Il peut créer, désactiver ou supprimer un compte. |
| **Scénario alternatif** | A1 : Tentative de suppression d'un compte lié à des données actives (offres, candidatures) → désactivation proposée au lieu de suppression définitive. |
| **Postcondition** | La liste des comptes est à jour. |

---

### UC17 — Gérer les rôles

| Élément | Détail |
|---|---|
| **Acteur** | Administrateur |
| **Précondition** | L'administrateur est connecté |
| **Scénario nominal** | 1. L'administrateur sélectionne un utilisateur.<br>2. Il modifie son rôle (Recruteur / Admin).<br>3. Le système met à jour les droits d'accès associés. |
| **Scénario alternatif** | A1 : Tentative de retirer le dernier compte Admin → action bloquée pour éviter un système sans administrateur. |
| **Postcondition** | Les droits d'accès de l'utilisateur sont mis à jour. |

---

### UC18 — Consulter les logs

| Élément | Détail |
|---|---|
| **Acteur** | Administrateur |
| **Précondition** | L'administrateur est connecté |
| **Scénario nominal** | 1. L'administrateur accède au journal des actions.<br>2. Le système affiche les actions (qui, quoi, quand) avec filtres (utilisateur, date, type d'action). |
| **Scénario alternatif** | — |
| **Postcondition** | L'administrateur peut auditer l'activité du système (traçabilité, RGPD). |

---

### UC19 — Consulter les statistiques

| Élément | Détail |
|---|---|
| **Acteur** | Administrateur |
| **Précondition** | L'administrateur est connecté |
| **Scénario nominal** | 1. L'administrateur accède au tableau de bord global.<br>2. Le système affiche : nombre d'offres actives, nombre de candidatures, taux d'acceptation, répartition des scores. |
| **Scénario alternatif** | — |
| **Postcondition** | L'administrateur dispose d'une vue globale sur l'activité de la plateforme. |

---

## Règles de gestion transversales

- **RG1** : Toute décision finale (accepter/rejeter) doit être validée par un humain avec justification écrite (principe *human-in-the-loop*).
- **RG2** : Un candidat ne peut postuler qu'une seule fois à la même offre.
- **RG3** : Toute action sensible (création de compte, modification de rôle, décision de recrutement) est enregistrée dans le journal des logs.
- **RG4** : Le score de matching est calculé automatiquement mais reste indicatif — il n'entraîne aucune décision automatique.
- **RG5** : Un compte administrateur ne peut pas être supprimé s'il est le dernier administrateur du système.
