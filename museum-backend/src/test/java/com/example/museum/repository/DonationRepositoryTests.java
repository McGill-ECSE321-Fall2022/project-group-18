package com.example.museum.repository;

import com.example.museum.model.Artifact;
import com.example.museum.model.Donation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Test of CustomerRepo
// Springboot testing setup
@SpringBootTest
public class DonationRepositoryTests {
    // Injecting the donationRepository
    @Autowired
    private DonationRepository donationRepository;
    // Injecting teh artifactRepository
    @Autowired
    private ArtifactRepository artifactRepository;

    // Clear the persistence data in database after each test
    @AfterEach
    public void clearDatabase() {
        donationRepository.deleteAll();
        artifactRepository.deleteAll();
    }

    // The test method - read and write the object, its reference and its attributes
    @Test
    public void testPersistAndLoadDonation() {
        // Create 1st artifact
        boolean loaned1 = true;
        boolean loanable1 = true;
        String name1 = "Mona Lisa";
        int loanFee1 = 5;
        Artifact.ArtType artType1 = Artifact.ArtType.Painting;
        // Create an artifact object with some attributes
        Artifact artifact1 = new Artifact();
        artifact1.setLoanable(loanable1);
        artifact1.setName(name1);
        artifact1.setType(artType1);
        artifact1.setLoaned(loaned1);
        artifact1.setLoanFee(loanFee1);

        // Create 2nd artifact
        boolean loaned2 = false;
        boolean loanable2 = false;
        String name2 = "David";
        int loanFee2 = 6;
        Artifact.ArtType artType2 = Artifact.ArtType.Sculpture;
        // Create an artifact object with some attributes
        Artifact artifact2 = new Artifact();
        artifact2.setLoanable(loanable2);
        artifact2.setName(name2);
        artifact2.setType(artType2);
        artifact2.setLoaned(loaned2);
        artifact2.setLoanFee(loanFee2);

        // Save the artifact in its database, tests its writing
        // Keep track with its automatically generated ID
        artifact1 = artifactRepository.save(artifact1);
        int artID1 = artifact1.getArtID();
        artifact2 = artifactRepository.save(artifact2);
        int artID2 = artifact2.getArtID();

        // Create donation
        Donation donation = new Donation();
        donation.setNewDonationArtifactsList();
        donation.addDonatedArtifact(artifact1);
        donation.addDonatedArtifact(artifact2);

        // Save Donation
        donation = donationRepository.save(donation);
        int donationID = donation.getDonationID();

        donation = null;
        artifact1 = null;
        artifact2 = null;
        // Read from the database using the id, test its reading
        donation = donationRepository.findDonationByDonationID(donationID);
        // Test if an object is returned
        assertNotNull(donation);
        // Test if the attributes read from DB is correct
        assertEquals(donationID, donation.getDonationID());
        List<Artifact> artifactArrayList = donation.getDonatedArtifacts();
        // Test if the associations are correct
        assertEquals(artifactArrayList.size(), 2);
        artifact1 = artifactArrayList.get(0);
        assertNotNull(artifact1);
        assertEquals(artID1, artifact1.getArtID());
        assertEquals(loanable1, artifact1.getLoanable());
        assertEquals(name1, artifact1.getName());
        assertEquals(artType1, artifact1.getType());
        assertEquals(loaned1, artifact1.getLoaned());
        assertEquals(loanFee1, artifact1.getLoanFee());
        artifact2 = artifactArrayList.get(1);
        assertNotNull(artifact2);
        assertEquals(artID2, artifact2.getArtID());
        assertEquals(loanable2, artifact2.getLoanable());
        assertEquals(name2, artifact2.getName());
        assertEquals(artType2, artifact2.getType());
        assertEquals(loaned2, artifact2.getLoaned());
        assertEquals(loanFee2, artifact2.getLoanFee());
    }
}
