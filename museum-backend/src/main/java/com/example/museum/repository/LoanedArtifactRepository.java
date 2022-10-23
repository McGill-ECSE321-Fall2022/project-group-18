package com.example.museum.repository;

import com.example.museum.model.LoanedArtifact;
import org.springframework.data.repository.CrudRepository;

public interface LoanedArtifactRepository extends CrudRepository<LoanedArtifact, Integer> {
    public LoanedArtifact findLoanedArtifactByLoanID(int id);
}
