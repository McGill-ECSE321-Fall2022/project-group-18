package com.example.museum.repository;

import com.example.museum.model.Artifact;
import com.example.museum.model.ArtifactAbs;
import com.example.museum.model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RoomRepositoryTests {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

    @AfterEach
    public void clearDatabase() {
        roomRepository.deleteAll();
        artifactRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadRoom() {
        // Create Artifact
        boolean loanable1 = true;
        ArtifactAbs.ArtType artType1 = ArtifactAbs.ArtType.Sculpture;
        String artName1 = "David";
        Artifact artifact1 = new Artifact();
        artifact1.setLoanable(loanable1);
        artifact1.setName(artName1);
        artifact1.setType(artType1);

        boolean loanable2 = false;
        Artifact.ArtType artType2 = Artifact.ArtType.Painting;
        String artName2 = "Portrait of Dr. Gachet";
        Artifact artifact2 = new Artifact();
        artifact2.setLoanable(loanable2);
        artifact2.setName(artName2);
        artifact2.setType(artType2);
        // Save Artifact
        artifact1 = artifactRepository.save((Artifact) artifact1);
        int artID1 = artifact1.getArtID();
        artifact2 = artifactRepository.save((Artifact) artifact2);
        int artID2 = artifact2.getArtID();
        // Create Room
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
        int roomID = room.getRoomID();
        // Read Room from DB
        room = null;
        artifact1 = null;
        artifact2 = null;
        room = roomRepository.findRoomByRoomID(roomID);
        // Assert that Room has correct attributes
        assertNotNull(room);
        assertEquals(roomID, room.getRoomID());
        List<Artifact> artifactArrayList = room.getRoomArtifacts();
        assertEquals(artifactArrayList.size(), 2);
        artifact1 = artifactArrayList.get(0);
        artifact2 = artifactArrayList.get(1);
        assertNotNull(artifact1);
        assertEquals(artID1, artifact1.getArtID());
        assertNotNull(artifact2);
        assertEquals(artID2, artifact2.getArtID());
    }
}
