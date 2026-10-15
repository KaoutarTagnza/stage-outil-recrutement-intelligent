package com.recrutementia.repository;

import com.recrutementia.model.Entretien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntretienRepository extends JpaRepository<Entretien, Long> {

    List<Entretien> findByCandidatureCandidatUtilisateurEmail(String email);

    Optional<Entretien> findByCandidatureId(Long candidatureId);
}