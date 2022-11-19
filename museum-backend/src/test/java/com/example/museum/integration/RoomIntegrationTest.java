package com.example.museum.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.example.museum.dto.ArtifactDto;
import com.example.museum.model.Artifact;
import com.example.museum.model.Room;
import com.example.museum.repository.ArtifactRepository;
import com.example.museum.repository.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomIntegrationTest {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        roomRepository.deleteAll();
        artifactRepository.deleteAll();
    }

    @Test
    public void testCreateRoom() {
        Room room = testCreateAValidRoom();
    }

    @Test
    public void testCreateRoomWithDuplicatedName() {
        Room room = testCreateAValidRoom();
        testCreateAnInvalidRoom();
    }

    @Test
    public void testCreateAndGetRoom() {
        Room room = testCreateAValidRoom();
        testGetARoomWithValidID(room.getRoomID());
    }

    @Test
    public void testCreateAndGetRoomWithInvalidID() {
        Room room = testCreateAValidRoom();
        testGetARoomWithInvalidID(room.getRoomID() + 1);
    }

    @Test
    public void testCreateAndGetAllRooms() {
        Room room = testCreateAValidRoom();
        Room room1 = testCreateAnotherValidRoom();
        List<Room> roomList = new ArrayList<>();
        roomList.add(room);
        roomList.add(room1);
        testGetAllRooms(roomList);
    }

    @Test
    public void testCreateRoomsAndAddArtifacts() {
        Room room = testCreateAValidRoom();
        List<Artifact> artifactList = createAValidArtifactList();
        testAddArtifactListIntoRoom(artifactList, room.getRoomID());
    }

    @Test
    public void testCreateRoomAndAddInvalidArtifacts() {
        Room room = testCreateARoomWithSmallCapacity();
        List<Artifact> artifactList = createAValidArtifactList();
        testAddArtifactListIntoRoomWithInsufficientCapacity(artifactList, room.getRoomID());
    }

    @Test
    public void testCreateRoomAndTransferArtifactsBetweenRooms() {
        Room room = testCreateAValidRoom();
        Room room1 = testCreateAnotherValidRoom();
        List<Artifact> artifactList = createAValidArtifactList();
        testAddArtifactListIntoRoom(artifactList, room.getRoomID());
        testTransferArtifactBetweenRooms(room.getRoomID(), room1.getRoomID(), artifactList.get(0).getArtID());
    }

    @Test
    public void testCreateRoomAndTransferArtifactsNotInSrcRoom() {
        Room room = testCreateAValidRoom();
        Room room1 = testCreateAnotherValidRoom();
        List<Artifact> artifactList = createAValidArtifactList();
        testAddArtifactListIntoRoom(artifactList, room1.getRoomID()); // add artifacts into dest room
        testTransferWithArtifactNotInSrcRoom(room.getRoomID(), room1.getRoomID(), artifactList.get(0).getArtID());
    }

    @Test
    public void testCreateRoomsAndArtifactsAndGetAll() {
        Room room = testCreateAValidRoom();

        List<Artifact> artifactList = createAValidArtifactList();
        testAddArtifactListIntoRoom(artifactList, room.getRoomID());

        testGetAllRoomsAndArtifacts(room, artifactList);
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

    private void testGetARoomWithValidID(int roomID) {
        String getRoomParam = "/room/" + roomID;
        ResponseEntity<Map> response = client.getForEntity(getRoomParam, Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Room room = roomRepository.findRoomByRoomID(roomID);
        assertEquals(room.getRoomID(), response.getBody().get("roomID"));
        assertEquals(room.getCapacity(), response.getBody().get("roomCapacity"));
        assertEquals(room.getName(), response.getBody().get("roomName"));
        assertEquals(room.getRoomArtifacts().size(), response.getBody().get("roomArtifactNum"));
        assertNotNull(response.getBody().get("roomArtifactIDList"));
    }

    private void testGetARoomWithInvalidID(int roomID) {
        String getRoomParam = "/room/" + roomID;
        ResponseEntity<String> response = client.getForEntity(getRoomParam, String.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Room not found", response.getBody());
    }

    private Room testCreateAnotherValidRoom() {
        final String roomName = "Storage";
        final int roomCapacity = -1;
        String createRoomParam = "/room?roomName=";
        createRoomParam += roomName;
        createRoomParam += "&roomCapacity=";
        createRoomParam += roomCapacity;
        ResponseEntity<Integer> response = client.getForEntity(createRoomParam, Integer.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(roomRepository.existsById(response.getBody()));
        return roomRepository.findRoomByRoomID(response.getBody());
    }

    private void testGetAllRooms(List<Room> roomList) {
        String getRoomParam = "/room/all";
        ResponseEntity<List> response = client.getForEntity(getRoomParam, List.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Integer> roomIDList = response.getBody();
        assertEquals(roomList.size(), roomIDList.size());
        for (Room room: roomList) {
            assertTrue(roomIDList.contains(room.getRoomID()));
        }
    }

    private List<Artifact> createAValidArtifactList() {
        final String name1 = "Mona Lisa";
        final Artifact.ArtType type1 = Artifact.ArtType.Painting;
        final boolean loanable1 = true;
        final boolean loaned1 = false;
        final int loanFee1 = 100;
        Artifact artifact1 = new Artifact();
        artifact1.setName(name1);
        artifact1.setType(type1);
        artifact1.setLoanable(loanable1);
        artifact1.setLoaned(loaned1);
        artifact1.setLoanFee(loanFee1);
        ArtifactDto artifactDto1 = new ArtifactDto(artifact1);
        ResponseEntity<ArtifactDto> response1 = client.postForEntity("/artifact", artifactDto1, ArtifactDto.class);

        List<Artifact> artifactList = new ArrayList<>();
        artifactList.add(response1.getBody().toModel());

        final String name2 = "David";
        final Artifact.ArtType type2 = Artifact.ArtType.Sculpture;
        final boolean loanable2 = true;
        final boolean loaned2 = false;
        final int loanFee2 = 120;
        Artifact artifact2 = new Artifact();
        artifact2.setName(name2);
        artifact2.setType(type2);
        artifact2.setLoanable(loanable2);
        artifact2.setLoaned(loaned2);
        artifact2.setLoanFee(loanFee2);
        ArtifactDto artifactDto2 = new ArtifactDto(artifact2);
        ResponseEntity<ArtifactDto> response2 = client.postForEntity("/artifact", artifactDto2, ArtifactDto.class);
        artifactList.add(response2.getBody().toModel());
        return artifactList;
    }

    private void testAddArtifactListIntoRoom(List<Artifact> artifactList, int roomID) {
        String addArtifactToRoomParam = "/room/artifacts/add?roomID=";
        addArtifactToRoomParam += roomID;
        List<Integer> artifactIDList = new ArrayList<>();
        for (Artifact artifact: artifactList) {
            artifactIDList.add(artifact.getArtID());
        }
        addArtifactToRoomParam += "&artifactIDList=";

        for (int i = 0; i < artifactIDList.size(); i++) {
            addArtifactToRoomParam = addArtifactToRoomParam + artifactIDList.get(i).toString();
            if (i == artifactIDList.size()-1) {
                continue;
            }
            addArtifactToRoomParam = addArtifactToRoomParam + ",";
        }
        ResponseEntity<Map> response = client.getForEntity(addArtifactToRoomParam, Map.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(artifactList.size(), response.getBody().get("roomArtifactNum"));
        List<Integer> artifactIDListFromResponse = (List<Integer>) response.getBody().get("roomArtifactIDList");
        for (int artifactID: artifactIDList) {
            assertTrue(artifactIDListFromResponse.contains(artifactID));
        }
    }

    private Room testCreateARoomWithSmallCapacity() {
        final String roomName = "SSR1";
        final int roomCapacity = 1;
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

    private void testAddArtifactListIntoRoomWithInsufficientCapacity(List<Artifact> artifactList, int roomID) {
        String addArtifactToRoomParam = "/room/artifacts/add?roomID=";
        addArtifactToRoomParam += roomID;
        List<Integer> artifactIDList = new ArrayList<>();
        for (Artifact artifact: artifactList) {
            artifactIDList.add(artifact.getArtID());
        }
        addArtifactToRoomParam += "&artifactIDList=";

        for (int i = 0; i < artifactIDList.size(); i++) {
            addArtifactToRoomParam = addArtifactToRoomParam + artifactIDList.get(i).toString();
            if (i == artifactIDList.size()-1) {
                continue;
            }
            addArtifactToRoomParam = addArtifactToRoomParam + ",";
        }
        ResponseEntity<String> response = client.getForEntity(addArtifactToRoomParam, String.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Display room's artifacts cannot exceed its capacity", response.getBody());
    }

    private void testTransferArtifactBetweenRooms(int srcRoomID, int destRoomID, int artifactID) {
        String transferParam = "/room/artifacts/move?srcRoomID=";
        transferParam += srcRoomID;
        transferParam += "&destRoomID=";
        transferParam += destRoomID;
        transferParam += "&artifactID=";
        transferParam += artifactID;
        ResponseEntity<Map> response = client.getForEntity(transferParam, Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().get("srcRoomArtifactNum"));
        assertEquals(1, response.getBody().get("destRoomArtifactNum"));
        List<Integer> destRoomArtifactIDList = (List<Integer>) response.getBody().get("destRoomArtifactIDList");
        assertTrue(destRoomArtifactIDList.contains(artifactID));
    }

    private void testTransferWithArtifactNotInSrcRoom(int srcRoomID, int destRoomID, int artifactID) {
        String transferParam = "/room/artifacts/move?srcRoomID=";
        transferParam += srcRoomID;
        transferParam += "&destRoomID=";
        transferParam += destRoomID;
        transferParam += "&artifactID=";
        transferParam += artifactID;
        ResponseEntity<String> response = client.getForEntity(transferParam, String.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("The artifact conflicts with this transfer", response.getBody());

    }

    private void testGetAllRoomsAndArtifacts(Room room, List<Artifact> artifactList) {
        String getAllRoomsArtifactsParam = "/room/all/artifacts";
        ResponseEntity<Map> response = client.getForEntity(getAllRoomsArtifactsParam, Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, Integer> allRoomsArtifactsMap = response.getBody();
        assertEquals(artifactList.size(), allRoomsArtifactsMap.size());
        assertEquals(room.getRoomID(), allRoomsArtifactsMap.get(Integer.toString(artifactList.get(0).getArtID())));
        assertEquals(room.getRoomID(), allRoomsArtifactsMap.get(Integer.toString(artifactList.get(0).getArtID())));
    }


}
