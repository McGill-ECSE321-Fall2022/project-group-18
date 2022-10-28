package com.example.museum.repository;

import com.example.museum.model.Artifact;
import com.example.museum.model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Test of RoomRepository
// Springboot testing setup
@SpringBootTest
public class RoomRepositoryTests {

    // Injecting the RoomRepository
    @Autowired
    private RoomRepository roomRepository;

    // Injecting the ArtifactRepository
    @Autowired
    private ArtifactRepository artifactRepository;

    // Clear the persistence data in database after each test
    @AfterEach
    public void clearDatabase() {
        roomRepository.deleteAll();
        artifactRepository.deleteAll();
    }

    // The test method - read and write the object, its reference and its attributes
    @Test
    public void testPersistAndLoadRoom() {
        // Create Artifact with attributes
        boolean loanable1 = true;
        Artifact.ArtType artType1 = Artifact.ArtType.Sculpture;
        String artName1 = "David";
        boolean loaned1 = false;
        Artifact artifact1 = new Artifact();
        artifact1.setLoanable(loanable1);
        artifact1.setName(artName1);
        artifact1.setType(artType1);
        artifact1.setLoaned(loaned1);

        // Create another Artifact object with attributes
        boolean loanable2 = false;
        Artifact.ArtType artType2 = Artifact.ArtType.Painting;
        String artName2 = "Portrait of Dr. Gachet";
        boolean loaned2 = false;
        Artifact artifact2 = new Artifact();
        artifact2.setLoanable(loanable2);
        artifact2.setName(artName2);
        artifact2.setType(artType2);
        artifact2.setLoaned(loaned2);
        // Save Artifact
        artifact1 = artifactRepository.save((Artifact) artifact1);
        int artID1 = artifact1.getArtID();
        artifact2 = artifactRepository.save((Artifact) artifact2);
        int artID2 = artifact2.getArtID();
        // Create Room with attributes and associations
        String roomName = "Large Display Room 1";
        int roomCapacity = 300;
        Room room = new Room();
        room.setName(roomName);
        room.setCapacity(roomCapacity);
        room.setNewRoomArtifactsList();
        room.addRoomArtifact((Artifact) artifact1);
        room.addRoomArtifact((Artifact) artifact2);
        // Save Room
        room = roomRepository.save(room);
        // Keep track of its automatically generated ID
        int roomID = room.getRoomID();
        // Read Room from DB
        room = null;
        artifact1 = null;
        artifact2 = null;
        room = roomRepository.findRoomByRoomID(roomID);
        // Assert that Room has correct attributes
        // Test if the return is an object
        assertNotNull(room);
        // Test if the attributes are correct
        assertEquals(roomID, room.getRoomID());
        assertEquals(roomName, room.getName());
        assertEquals(roomCapacity, room.getCapacity());
        // Test if the room has correct associations
        List<Artifact> artifactArrayList = room.getRoomArtifacts();
        assertEquals(artifactArrayList.size(), 2);
        artifact1 = artifactArrayList.get(0);
        artifact2 = artifactArrayList.get(1);
        // Test if association exists
        assertNotNull(artifact1);
        // Test if the attribtues of associated objects are correct
        assertEquals(artID1, artifact1.getArtID());
        assertEquals(artName1, artifact1.getName());
        assertEquals(loanable1, artifact1.getLoanable());
        assertEquals(loaned1, artifact1.getLoaned());
        assertEquals(artType1, artifact1.getType());
        // Repeat for artifact 2
        assertNotNull(artifact2);
        assertEquals(artID2, artifact2.getArtID());
        assertEquals(artName2, artifact2.getName());
        assertEquals(loanable2, artifact2.getLoanable());
        assertEquals(loaned2, artifact2.getLoaned());
        assertEquals(artType2, artifact2.getType());
    }
}
