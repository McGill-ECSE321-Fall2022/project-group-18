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
        boolean loanable1 = true;
        Artifact.ArtType artType1 = Artifact.ArtType.Sculpture;
        String artName1 = "David";
        Artifact artifact1 = new Artifact();
        artifact1.setLoanable(loanable1);
        artifact1.setName(artName1);
        artifact1.setType(artType1);

        boolean loanable2 = true;
        Artifact.ArtType artType2 = Artifact.ArtType.Painting;
        String artName2 = "Portrait of Dr. Gachet";
        Artifact artifact2 = new Artifact();
        artifact2.setLoanable(loanable2);
        artifact2.setName(artName2);
        artifact2.setType(artType2);
        // Save Artifact
        artifact1 = artifactRepository.save(artifact1);
        int artID1 = artifact1.getArtID();
        artifact2 = artifactRepository.save(artifact2);
        int artID2 = artifact2.getArtID();
        // Create LoanRequest
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setNewRequestedArtifactsList();
        loanRequest.addRequestedArtifact(artifact1);
        loanRequest.addRequestedArtifact(artifact2);
        // Save LoanRequest
        loanRequest = loanRequestRepository.save(loanRequest);
        int loanRequestID = loanRequest.getRequestID();
        // Read LoanRequest from DB
        loanRequest = null;
        artifact1 = null;
        artifact2 = null;
        loanRequest = loanRequestRepository.findLoanRequestByRequestID(loanRequestID);
        // Assert that Room has correct attributes
        assertNotNull(loanRequest);
        assertEquals(loanRequestID, loanRequest.getRequestID());
        assertNotNull(loanRequest.getRequestedArtifact(0).getArtID());
        assertEquals(artID1, loanRequest.getRequestedArtifact(0).getArtID());
        assertNotNull(loanRequest.getRequestedArtifact(1));
        assertEquals(artID2, loanRequest.getRequestedArtifact(1).getArtID());
    }

}
