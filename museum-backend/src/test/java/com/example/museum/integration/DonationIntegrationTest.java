package com.example.museum.integration;

import com.example.museum.dto.DonationDto;
import com.example.museum.model.Artifact;
import com.example.museum.model.Donation;
import com.example.museum.repository.ArtifactRepository;
import com.example.museum.repository.DonationRepository;
import org.apache.coyote.ajp.AjpAprProtocol;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DonationIntegrationTest {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase(){
        donationRepository.deleteAll();
    }

    @Test
    public void testCreateAndGetDonation(){
        int id = testCreateDonation();
//        testGetDonation(id);
//        testGetArtifactsInDonation(id);
    }

    private int testCreateDonation() {
        final String name = "David";
        final Artifact.ArtType type = Artifact.ArtType.Painting;
        final boolean loanable = true;
        final boolean loaned = false;
        final int loanFee = 87;

        Artifact art = new Artifact(1, name, type, loanable, loaned, loanFee);
        art = artifactRepository.save(art);
        List<Artifact> arts = new ArrayList<>();
        arts.add(art);

        Donation donation = new Donation(0);
        donation.addDonatedArtifact(art);

        final DonationDto donationDto = new DonationDto(donation);

        ResponseEntity<DonationDto> response = client.postForEntity("/donation", donationDto, DonationDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getDonationID() > 0);

        return response.getBody().getDonationID();
    }
}
