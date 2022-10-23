package com.example.museum.repository;

import com.example.museum.model.ArtifactAbs;
import com.example.museum.model.LoanedArtifact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface LoanedArtifactRepository extends ArtifactAbsRepository<LoanedArtifact> ,  CrudRepository<LoanedArtifact, Integer> {
}
