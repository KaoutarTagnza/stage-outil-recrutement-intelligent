package com.recrutementia.repository;

import com.recrutementia.model.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    List<Candidature> findByOffreId(Long offreId);
    List<Candidature> findByCandidatId(Long candidatId);
    Optional<Candidature> findByCandidatIdAndOffreId(Long candidatId, Long offreId);
}
