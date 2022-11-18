package com.example.museum.integration;

import com.example.museum.dto.ArtifactDto;
import com.example.museum.model.Artifact;
import com.example.museum.repository.ArtifactRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArtifactIntegrationTest {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private ArtifactRepository artifactRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase(){
        artifactRepository.deleteAll();
    }

    @Test
    public void testCreateAndGetArtifact(){
        int id = testCreateArtifact();
        testGetArtifact(id);
        testUpdateArtifact(id);
        testGetAllArtifacts(id);
        testGetLoanableArtifacts(id);
    }

    private int testCreateArtifact(){
        final String name = "David";
        final Artifact.ArtType type = Artifact.ArtType.Painting;
        final boolean loanable = false;
        final boolean loaned = true;
        final int loanFee = 87;
        final ArtifactDto artifactDto = new ArtifactDto( new Artifact(0,name,type,loanable,loaned,loanFee));

        ResponseEntity<ArtifactDto> response = client.postForEntity("/artifact", artifactDto, ArtifactDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertTrue(response.getBody().getArtID() > 0, "Response has correct ID");
        assertEquals(name, response.getBody().getName(), "Response has correct name");
        assertEquals(type, response.getBody().getType(),"Response has correct type");
        assertEquals(loanable, response.getBody().getLoanable(), "Response has correct loanable");
        assertEquals(loaned, response.getBody().getLoaned(), "Response has correct loaned");
        assertEquals(loanFee, response.getBody().getLoanFee(), "Response has correct loanFee");

        return response.getBody().getArtID();

    }

    private void testGetArtifact(int id){
        final String name = "David";
        final Artifact.ArtType type = Artifact.ArtType.Painting;
        final boolean loanable = false;
        final boolean loaned = true;
        final int loanFee = 87;

        ResponseEntity<ArtifactDto> response = client.getForEntity("/artifact/" + id, ArtifactDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correctStatus");
        assertNotNull(response.getBody(), "Response has body");
        assertTrue(response.getBody().getArtID() == id, "Response has correct ID");
        assertEquals("David", response.getBody().getName(), "Response has correct name");
        assertEquals(Artifact.ArtType.Painting, response.getBody().getType(), "Response has correct type");
        assertEquals(false, response.getBody().getLoanable(), "Response has correct loanable");
        assertEquals(true, response.getBody().getLoaned(), "Response has correct loaned");
        assertEquals(87, response.getBody().getLoanFee(), "Response has correct loanFee");
    }

    private void testUpdateArtifact(int id){
        ResponseEntity<ArtifactDto> response = client.getForEntity("/artifact/" + id, ArtifactDto.class);
        final boolean prevLoanable = response.getBody().getLoanable();
        final boolean updatedLoanable = true;
        final boolean prevLoaned = response.getBody().getLoaned();
        final boolean updatedLoaned = false;
        final int prevLoanFee = response.getBody().getLoanFee();
        final int updatedLoanFee = 100;

        final ArtifactDto artifactDto = new ArtifactDto(new Artifact(0, response.getBody().getName(), response.getBody().getType(), updatedLoanable, updatedLoaned, updatedLoanFee));

        ResponseEntity<ArtifactDto> response2 = client.postForEntity("/artifact/update/" + id, artifactDto, ArtifactDto.class);

        assertNotNull(response2);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertNotNull(response2.getBody());
        assertEquals(id,response2.getBody().getArtID());
        assertNotEquals(prevLoanable, response2.getBody().getLoanable());
        assertNotEquals(prevLoaned,response2.getBody().getLoaned());
        assertNotEquals(prevLoanFee,response2.getBody().getLoanFee());

    }

    public void testGetAllArtifacts(int id){
        final String name = "David";
        final Artifact.ArtType type = Artifact.ArtType.Painting;
        final boolean loanable = true;
        final boolean loaned = false;
        final int loanFee = 100;

        ResponseEntity<List<ArtifactDto>> responseEntity = client.exchange("/artifact/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<ArtifactDto>>() {});

        List<ArtifactDto> response = responseEntity.getBody();

        assertNotNull(response);
        assertEquals(1,response.size());
        assertEquals(name, response.get(0).getName());
        assertEquals(type, response.get(0).getType());
        assertEquals(loanable, response.get(0).getLoanable());
        assertEquals(loaned, response.get(0).getLoaned());
        assertEquals(loanFee, response.get(0).getLoanFee());
    }

    public void testGetLoanableArtifacts(int id){
        final String name = "David";
        final Artifact.ArtType type = Artifact.ArtType.Painting;
        final boolean loanable = true;
        final boolean loaned = false;
        final int loanFee = 100;

        ResponseEntity<List<ArtifactDto>> responseEntity = client.exchange("/loanableArtifact/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<ArtifactDto>>() {});

        List<ArtifactDto> response = responseEntity.getBody();

        assertNotNull(response);
        assertEquals(1,response.size());
        assertEquals(name, response.get(0).getName());
        assertEquals(type, response.get(0).getType());
        assertEquals(loanable, response.get(0).getLoanable());
        assertEquals(loaned, response.get(0).getLoaned());
        assertEquals(loanFee, response.get(0).getLoanFee());
    }



//    @Test
//    public void testCreateInvalidArtifact(){
//        ResponseEntity<String> response = client.postForEntity("/artifact", new ArtifactDto(" ",Artifact.ArtType.Sculpture,true,false,10),String.class);
//        assertNotNull(response);
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
//    }

//    @Test
//    public void testGetAllArtifacts(){
//        ResponseEntity<List> response = client.getForEntity("/artifact/all", List.class);
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//    }

    @Test
    public void testGetInvalidArtifact(){
        ResponseEntity<String> response = client.getForEntity("/artifact/" + Integer.MAX_VALUE, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Response has correct status");
        assertEquals("Artifact not found", response.getBody(), "Response has correct error message");
    }

    @Test
    public void testUpdateInvalidArtifact(){
        int id = Integer.MAX_VALUE;
        ArtifactDto artifactDto = new ArtifactDto();
        try{
            ResponseEntity<ArtifactDto> response = client.postForEntity("/artifact/update/" + id, artifactDto, ArtifactDto.class);
            //we should not hit this line - an exception should be called before this
            assertEquals(1,2);
        }catch (Exception e){
            ResponseEntity<String> response = client.postForEntity("/artifact/update/" + id, artifactDto, String.class);
            assertNotNull(response);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }


}
