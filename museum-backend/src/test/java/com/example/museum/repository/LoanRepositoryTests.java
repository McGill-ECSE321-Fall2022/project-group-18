package com.example.museum.repository;

import com.example.museum.model.Loan;
import com.example.museum.model.Artifact;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Test of LoanRepository
// Springboot testing setup
@SpringBootTest
public class LoanRepositoryTests {
    // Injecting the LoanRepository
    @Autowired
    private LoanRepository loanRepository;
    // Injecting the ArtifactRepository
    @Autowired
    private ArtifactRepository artifactRepository;

    // Clear the persistence data in database after each test
    @AfterEach
    public void clearDatabase() {
        loanRepository.deleteAll();
        artifactRepository.deleteAll();
    }

    // The test method - read and write the object, its reference and its attributes
    @Test
    public void testPersistAndLoadLoanRequest() {
        // Create Artifact
        boolean loanable1 = true;
        boolean loaned1 = true;
        Artifact.ArtType artType1 = Artifact.ArtType.Sculpture;
        String artName1 = "David";
        // Create a new artifact object with some attributes
        Artifact artifact1 = new Artifact();
        artifact1.setLoanable(loanable1);
        artifact1.setName(artName1);
        artifact1.setType(artType1);
        artifact1.setLoaned(loaned1);

        boolean loaned2 = false;
        boolean loanable2 = true;
        Artifact.ArtType artType2 = Artifact.ArtType.Painting;
        String artName2 = "Portrait of Dr. Gachet";
        // Create an artifact object with some attributes
        Artifact artifact2 = new Artifact();
        artifact2.setLoanable(loanable2);
        artifact2.setName(artName2);
        artifact2.setType(artType2);
        artifact2.setLoaned(loaned2);
        // Save Artifact
        artifact1 = artifactRepository.save(artifact1);
        // Keep track with the automatically generated ID
        int artID1 = artifact1.getArtID();
        artifact2 = artifactRepository.save(artifact2);
        int artID2 = artifact2.getArtID();
        // Create LoanRequest
        int loanFee = 1000;
        boolean approved = true;
        Loan loan = new Loan();
        loan.setNewrequestedArtifactsList();
        loan.addRequestedArtifact(artifact1);
        loan.addRequestedArtifact(artifact2);
        loan.setLoanFee(loanFee);
        loan.setApproved(approved);
        // Save LoanRequest
        loan = loanRepository.save(loan);
        int loanID = loan.getRequestID();
        loan = null;
        artifact1 = null;
        artifact2 = null;
        // Read LoanRequest from DB
        loan = loanRepository.findLoanRequestByRequestID(loanID);
        // Test if an object is returned
        assertNotNull(loan);
        // Test if attributes are correct
        assertEquals(loanID, loan.getRequestID());
        assertEquals(loanFee, loan.getLoanFee());
        assertEquals(approved, loan.getApproved());
        // Test if associations are correct
        assertNotNull(loan.getRequestedArtifact(0));
        assertEquals(artID1, loan.getRequestedArtifact(0).getArtID());
        assertEquals(loanable1, loan.getRequestedArtifact(0).getLoanable());
        assertEquals(artName1, loan.getRequestedArtifact(0).getName());
        assertEquals(artType1, loan.getRequestedArtifact(0).getType());
        assertEquals(loaned1, loan.getRequestedArtifact(0).getLoaned());
        assertNotNull(loan.getRequestedArtifact(1));
        assertEquals(artID2, loan.getRequestedArtifact(1).getArtID());
        assertEquals(loanable2, loan.getRequestedArtifact(0).getLoanable());
        assertEquals(artName2, loan.getRequestedArtifact(0).getName());
        assertEquals(artType2, loan.getRequestedArtifact(0).getType());
        assertEquals(loaned2, loan.getRequestedArtifact(0).getLoaned());
    }

}
