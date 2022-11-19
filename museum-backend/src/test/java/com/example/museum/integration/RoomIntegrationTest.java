package com.example.museum.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.example.museum.model.Room;
import com.example.museum.repository.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomIntegrationTest {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        roomRepository.deleteAll();
    }

    @Test
    public void testCreateRoom() {
        Room room = testCreateAValidRoom();
        testCreateAnInvalidRoom();
    }

    @Test
    public void testCreateRoomWithDuplicatedName() {
        Room room = testCreateAValidRoom();
    }

    private Room testCreateAValidRoom() {
        final String roomName = "LR1";
        final int roomCapacity = 300;
        String createRoomParam = "/room?roomName=";
        createRoomParam += roomName;
        createRoomParam += "&roomCapacity=";
        createRoomParam += roomCapacity;
        ResponseEntity<Integer> response = client.getForEntity(createRoomParam, Integer.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(roomRepository.existsById(response.getBody()));
        Room room = roomRepository.findRoomByRoomID(response.getBody());
        return room;
    }

    private void testCreateAnInvalidRoom() {
        final String roomName = "LR1";
        final int roomCapacity = 300;
        String createRoomParam = "/room?roomName=";
        createRoomParam += roomName;
        createRoomParam += "&roomCapacity=";
        createRoomParam += roomCapacity;
        ResponseEntity<String> response = client.getForEntity(createRoomParam, String.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("This room name already exists", response.getBody());
    }


}
