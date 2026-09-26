package com.recrutementia.repository;

import com.recrutementia.model.Cv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CvRepository extends JpaRepository<Cv, Long> {
    Optional<Cv> findByCandidatureId(Long candidatureId);
}
