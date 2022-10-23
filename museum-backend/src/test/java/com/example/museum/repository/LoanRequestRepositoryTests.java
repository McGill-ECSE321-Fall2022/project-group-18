package com.example.museum.repository;

import com.example.museum.model.LoanRequest;
import com.example.museum.model.Artifact;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class LoanRequestRepositoryTests {

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

    @AfterEach
    public void clearDatabase() {
        loanRequestRepository.deleteAll();
        artifactRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadLoanRequest() {
        // Create Artifact
        
    }

}
