package com.example.museum.integration;

import com.example.museum.dto.ArtifactDto;
import com.example.museum.dto.DonationDto;
import com.example.museum.exceptions.DatabaseException;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.criteria.CriteriaBuilder;

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
        testGetDonation(id);
        testGetArtifactsInDonation(id);
    }

    private int testCreateDonation() {
        final String name = "David";
        final Artifact.ArtType type = Artifact.ArtType.Painting;
        final boolean loanable = true;
        final boolean loaned = false;
        final int loanFee = 87;



        Artifact art = new Artifact(0, name, type, loanable, loaned, loanFee);
        List<Artifact> arts = new ArrayList<>();
        arts.add(art);


        ResponseEntity<DonationDto> response = client.postForEntity("/donation", arts, DonationDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getDonationID() > 0);

        return response.getBody().getDonationID();
    }

    private void testGetDonation(int id){
        final String name = "David";
        final Artifact.ArtType type = Artifact.ArtType.Painting;
        final boolean loanable = true;
        final boolean loaned = false;
        final int loanFee = 87;


        ResponseEntity<DonationDto> response = client.getForEntity("/donation/" + id, DonationDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correctStatus");
        assertNotNull(response.getBody(), "Response has body");

        assertTrue(response.getBody().getArtifactList().get(0).getArtID() > 0, "Response has correct ID");
        assertEquals("David", response.getBody().getArtifactList().get(0).getName(), "Response has correct name");
        assertEquals(Artifact.ArtType.Painting, response.getBody().getArtifactList().get(0).getType(), "Response has correct type");
        assertEquals(true, response.getBody().getArtifactList().get(0).getLoanable(), "Response has correct loanable");
        assertEquals(false, response.getBody().getArtifactList().get(0).getLoaned(), "Response has correct loaned");
        assertEquals(87, response.getBody().getArtifactList().get(0).getLoanFee(), "Response has correct loanFee");

    }

    private void testGetArtifactsInDonation(int id){
        final String name = "David";
        final Artifact.ArtType type = Artifact.ArtType.Painting;
        final boolean loanable = true;
        final boolean loaned = false;
        final int loanFee = 87;


        ResponseEntity<List<ArtifactDto>> responseEntity = client.exchange("/donation/artifacts/" + id , HttpMethod.GET, null, new ParameterizedTypeReference<List<ArtifactDto>>() {
        });

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        List<ArtifactDto> response = responseEntity.getBody();

        assertNotNull(response);
        assertEquals(1,response.size());
        assertEquals(name,response.get(0).getName());
        assertTrue(response.get(0).getArtID() > 0);
        assertEquals(type,response.get(0).getType());
        assertEquals(loanable,response.get(0).getLoanable());
        assertEquals(loaned, response.get(0).getLoaned());
        assertEquals(loanFee,response.get(0).getLoanFee());


    }

    @Test
    public void testGetInvalidDonation(){
        ResponseEntity<String> response = client.getForEntity("/donation/" + Integer.MAX_VALUE,String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Donation not found", response.getBody());
    }


}
