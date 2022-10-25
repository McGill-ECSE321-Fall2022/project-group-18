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


@SpringBootTest
public class DonationRepositoryTests {
    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

    @AfterEach
    public void clearDatabase() {
        donationRepository.deleteAll();
        artifactRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadDonation() {
        // Create 1st artifact
        boolean loanable1 = true;
        String name1 = "Mona Lisa";
        Artifact.ArtType artType1 = Artifact.ArtType.Painting;
        Artifact artifact1 = new Artifact();
        artifact1.setLoanable(loanable1);
        artifact1.setName(name1);
        artifact1.setType(artType1);

        // Create 2nd artifact
        boolean loanable2 = false;
        String name2 = "David";
        Artifact.ArtType artType2 = Artifact.ArtType.Sculpture;
        Artifact artifact2 = new Artifact();
        artifact2.setLoanable(loanable2);
        artifact2.setName(name2);
        artifact2.setType(artType2);

        // Save artifact

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
        donation = donationRepository.findDonationByDonationID(donationID);

        //Asserts

        assertNotNull(donation);
        assertEquals(donationID, donation.getDonationID());

        List<Artifact> artifactArrayList = donation.getDonatedArtifacts();
        assertEquals(artifactArrayList.size(), 2);

        artifact1 = (Artifact) artifactArrayList.get(0);
        assertNotNull(artifact1);
        assertEquals(artID1, artifact1.getArtID());


        artifact2 = (Artifact) artifactArrayList.get(1);
        assertNotNull(artifact2);
        assertEquals(artID2, artifact2.getArtID());

    }
}
