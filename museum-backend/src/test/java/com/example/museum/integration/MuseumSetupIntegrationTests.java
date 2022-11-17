package com.example.museum.integration;

import com.example.museum.dto.BusinessHourDto;
import com.example.museum.dto.RoomDto;
import com.example.museum.repository.OwnerRepository;
import com.example.museum.repository.RoomRepository;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MuseumSetupIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase(){
        roomRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    @Test
    public void testSetup(){
        testSetupSuccessful();
        testSetupAlreadyComplete();
    }

    private void testSetupSuccessful(){
        ResponseEntity<Boolean> response = client.postForEntity("/setup", Object.class,  Boolean.class);

        assertNotNull(response);
        assertEquals(Boolean.TRUE, response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    private void testSetupAlreadyComplete(){
        ResponseEntity<Boolean> response = client.postForEntity("/setup", Object.class,  Boolean.class);

        assertNotNull(response);
        assertEquals(Boolean.FALSE, response.getBody());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}
