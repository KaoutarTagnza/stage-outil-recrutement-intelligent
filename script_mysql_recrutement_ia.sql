-- =========================================================
-- Script de création de la base de données
-- Plateforme de recrutement intelligente (IA + Cybersécurité)
-- =========================================================

CREATE DATABASE IF NOT EXISTS recrutement_ia
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE recrutement_ia;

-- ---------------------------------------------------------
-- UTILISATEUR (Admin / Recruteur / Candidat, via colonne role)
-- ---------------------------------------------------------
CREATE TABLE utilisateur (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom           VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL UNIQUE,
    mot_de_passe  VARCHAR(255) NOT NULL,
    role          ENUM('ADMIN', 'RECRUTEUR', 'CANDIDAT') NOT NULL,
    actif         BOOLEAN DEFAULT TRUE,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ---------------------------------------------------------
-- CANDIDAT (sous-type d'UTILISATEUR : authentification unifiée)
-- ---------------------------------------------------------
CREATE TABLE candidat (
    id         BIGINT PRIMARY KEY,
    telephone  VARCHAR(20),
    CONSTRAINT fk_candidat_utilisateur FOREIGN KEY (id)
        REFERENCES utilisateur(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------
-- COMPETENCE
-- ---------------------------------------------------------
CREATE TABLE competence (
    id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- ---------------------------------------------------------
-- OFFRE
-- ---------------------------------------------------------
CREATE TABLE offre (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    titre           VARCHAR(150) NOT NULL,
    description     TEXT,
    date_creation   DATETIME DEFAULT CURRENT_TIMESTAMP,
    statut          ENUM('BROUILLON', 'PUBLIEE', 'FERMEE') DEFAULT 'BROUILLON',
    date_limite     DATETIME,
    recruteur_id    BIGINT NOT NULL,
    CONSTRAINT fk_offre_recruteur FOREIGN KEY (recruteur_id)
        REFERENCES utilisateur(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------
-- OFFRE_COMPETENCE (association many-to-many)
-- ---------------------------------------------------------
CREATE TABLE offre_competence (
    offre_id      BIGINT NOT NULL,
    competence_id BIGINT NOT NULL,
    PRIMARY KEY (offre_id, competence_id),
    CONSTRAINT fk_oc_offre FOREIGN KEY (offre_id)
        REFERENCES offre(id) ON DELETE CASCADE,
    CONSTRAINT fk_oc_competence FOREIGN KEY (competence_id)
        REFERENCES competence(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------
-- CANDIDAT_COMPETENCE (association many-to-many)
-- ---------------------------------------------------------
CREATE TABLE candidat_competence (
    candidat_id   BIGINT NOT NULL,
    competence_id BIGINT NOT NULL,
    PRIMARY KEY (candidat_id, competence_id),
    CONSTRAINT fk_cc_candidat FOREIGN KEY (candidat_id)
        REFERENCES candidat(id) ON DELETE CASCADE,
    CONSTRAINT fk_cc_competence FOREIGN KEY (competence_id)
        REFERENCES competence(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------
-- CANDIDATURE
-- ---------------------------------------------------------
CREATE TABLE candidature (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_depot   DATETIME DEFAULT CURRENT_TIMESTAMP,
    statut       ENUM('EN_ATTENTE', 'PRESELECTIONNEE', 'ENTRETIEN_PLANIFIE',
                       'ACCEPTEE', 'REJETEE') DEFAULT 'EN_ATTENTE',
    justification TEXT,
    candidat_id  BIGINT NOT NULL,
    offre_id     BIGINT NOT NULL,
    CONSTRAINT fk_candidature_candidat FOREIGN KEY (candidat_id)
        REFERENCES candidat(id) ON DELETE CASCADE,
    CONSTRAINT fk_candidature_offre FOREIGN KEY (offre_id)
        REFERENCES offre(id) ON DELETE CASCADE,
    CONSTRAINT uq_candidat_offre UNIQUE (candidat_id, offre_id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------
-- CV
-- ---------------------------------------------------------
CREATE TABLE cv (
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    fichier_url            VARCHAR(255) NOT NULL,
    competences_extraites  TEXT,
    experience_extraite    TEXT,
    resume_ia              TEXT,
    score_matching         DECIMAL(5,2) DEFAULT 0,
    candidature_id         BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_cv_candidature FOREIGN KEY (candidature_id)
        REFERENCES candidature(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------
-- ENTRETIEN
-- ---------------------------------------------------------
CREATE TABLE entretien (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_entretien      DATETIME NOT NULL,
    questions_generees  TEXT,
    notes               TEXT,
    candidature_id      BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_entretien_candidature FOREIGN KEY (candidature_id)
        REFERENCES candidature(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------
-- EVALUATION
-- ---------------------------------------------------------
CREATE TABLE evaluation (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    note_globale  DECIMAL(4,2),
    commentaire   TEXT,
    decision      ENUM('FAVORABLE', 'DEFAVORABLE', 'A_REVOIR'),
    entretien_id  BIGINT NOT NULL UNIQUE,
    recruteur_id  BIGINT NOT NULL,
    CONSTRAINT fk_evaluation_entretien FOREIGN KEY (entretien_id)
        REFERENCES entretien(id) ON DELETE CASCADE,
    CONSTRAINT fk_evaluation_recruteur FOREIGN KEY (recruteur_id)
        REFERENCES utilisateur(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------
-- NOTIFICATION
-- ---------------------------------------------------------
CREATE TABLE notification (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    message      VARCHAR(255) NOT NULL,
    type         ENUM('EMAIL', 'SYSTEME') DEFAULT 'SYSTEME',
    date_envoi   DATETIME DEFAULT CURRENT_TIMESTAMP,
    lu           BOOLEAN DEFAULT FALSE,
    candidat_id  BIGINT NOT NULL,
    CONSTRAINT fk_notification_candidat FOREIGN KEY (candidat_id)
        REFERENCES candidat(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------
-- LOGACTION
-- ---------------------------------------------------------
CREATE TABLE log_action (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    action         VARCHAR(255) NOT NULL,
    details        TEXT,
    adresse_ip     VARCHAR(45),
    date_heure     DATETIME DEFAULT CURRENT_TIMESTAMP,
    utilisateur_id BIGINT NOT NULL,
    CONSTRAINT fk_log_utilisateur FOREIGN KEY (utilisateur_id)
        REFERENCES utilisateur(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------
-- Index utiles pour les performances
-- ---------------------------------------------------------
CREATE INDEX idx_candidature_statut ON candidature(statut);
CREATE INDEX idx_offre_statut ON offre(statut);
CREATE INDEX idx_cv_score ON cv(score_matching);
