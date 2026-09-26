package com.recrutementia.repository;

import com.recrutementia.model.Offre;
import com.recrutementia.model.StatutOffre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OffreRepository extends JpaRepository<Offre, Long> {
    List<Offre> findByStatut(StatutOffre statut);
    List<Offre> findByRecruteurId(Long recruteurId);
}
